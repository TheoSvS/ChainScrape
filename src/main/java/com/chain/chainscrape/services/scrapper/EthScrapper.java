package com.chain.chainscrape.services.scrapper;

import com.chain.chainscrape.Provider;
import com.chain.chainscrape.Utils;
import com.chain.chainscrape.model.EPriceUnit;
import com.chain.chainscrape.model.data.EthData;
import com.chain.chainscrape.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
public class EthScrapper extends AbstractScrapper<EthData, EthBlock.Block> {
    private final LinkedHashMap<Web3j,String> rpcServices = new LinkedHashMap<>();

    private final String priceFeedFunctionName = "latestAnswer";
    private final String CALLER_ADDRESS = "0x79cDA9334fDDd435e8a5B291C659fF9d4c8B8899"; //my CLBoot1 address
    private final String CHAINLINK_ETH_USD_PRICE_FEED_CONTRACT = "0x5f4eC3Df9cbd43714FE2740f5E3616155c5b8419";

    public EthScrapper(Provider provider) {
        rpcServices.put(Web3j.build(new HttpService(provider.getINFURA_URL())), "Infura");
        rpcServices.put(Web3j.build(new HttpService(provider.getALCHEMY_URL())), "Alchemy");
    }

    @Override
    public void run() {
        try {
            latestCachedData = aggregateLatestData();
            log.info("Current Ethereum Block Number: " + latestCachedData.getBlock().getNumber());

        } catch (Error |
                 Exception e) { //intercept all exceptions, if a runnable within executor service fails, it won't print in default console by default
            log.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * Return the latestCachedBlock in order to avoid unnecessary calls to the blockchain.
     * New blocks are generated about once every 10 seconds, facilitated by the executor service.
     *
     * @return the latestCachedBlock
     */
    public EthData retrieveLatestCachedData() {
        try {
            return latestCachedData != null ? latestCachedData : aggregateLatestData();
        } catch (Error | IOException e) {
            log.error("Block couldn't be retrieved! Did you add your key in chain_infra_api_url.properties?" + System.lineSeparator() + ExceptionUtils.getStackTrace(e));
        }
        return null;
    }


    /** Tries to collect data from doing On-chain calls
     * @return
     * @throws IOException
     */
    protected EthData aggregateLatestData() throws IOException {
        for (Map.Entry<Web3j,String> rpcEntry : rpcServices.entrySet()) {
            try {
                Web3j web3jRPCservice = rpcEntry.getKey();
                EthBlock.Block latestBlock = pollLatestBlock(web3jRPCservice);
                Price latestPrice = pollLatestPricing(web3jRPCservice);
                return new EthData(latestBlock,latestPrice);
            } catch (Exception e) {
                log.error("Couldn't connect with " + rpcEntry.getValue() + "! Is a valid API key defined in chain_infra_api_url.properties?" + System.lineSeparator() + ExceptionUtils.getStackTrace(e));
            }
        }
        log.error("Couldn't connect with any RPC service!");
        return null;
    }

    @Override
    protected Price pollLatestPricing(Web3j web3jRPCservice) throws InterruptedException, ExecutionException {
        Function function = new Function(
                priceFeedFunctionName,
                List.of(),  // Solidity Types in smart contract functions. Our price feed call doesn't need input as per the contract
                List.of(new TypeReference<Int256>(){})); //The output Types of the response

        String encodedFunction = FunctionEncoder.encode(function);
        EthCall response = web3jRPCservice.ethCall(
                Transaction.createEthCallTransaction(CALLER_ADDRESS, CHAINLINK_ETH_USD_PRICE_FEED_CONTRACT, encodedFunction), DefaultBlockParameterName.LATEST)
     .sendAsync().get();

        BigInteger price8Decimals = (BigInteger) FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters()).get(0).getValue();
        BigDecimal price = Utils.ethDecToBigDecimal(price8Decimals,8,2);
        return new Price(price, EPriceUnit.USD);
    }

    /**
     * Retrieves latest block from the blockchain.
     *
     * @return the block
     * @throws IOException
     */
    @Override
    protected synchronized EthBlock.Block pollLatestBlock(Web3j web3jRPCservice) throws IOException {
        long blockNumber = web3jRPCservice.ethBlockNumber().send().getBlockNumber().longValue(); // Latest block
        EthBlock.Block block = web3jRPCservice.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), true).send().getBlock();
        return block;
    }


    @Override
    public void cleanup() {
        rpcServices.keySet().forEach(Web3j::shutdown);
    }
}