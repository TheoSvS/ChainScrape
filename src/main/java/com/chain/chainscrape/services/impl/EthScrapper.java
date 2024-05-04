package com.chain.chainscrape.services.impl;

import com.chain.chainscrape.Provider;
import com.chain.chainscrape.model.EPriceUnit;
import com.chain.chainscrape.model.EthData;
import com.chain.chainscrape.model.Price;
import com.chain.chainscrape.services.AbstractScrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class EthScrapper extends AbstractScrapper<EthData, EthBlock.Block> {
    private final LinkedHashMap<Web3j,String> rpcServices = new LinkedHashMap<>();

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


    protected EthData aggregateLatestData() throws IOException {
        return new EthData(pollLatestBlock(), pollLatestPricing());
    }

    /**
     * Retrieves latest block from the blockchain.
     *
     * @return the block
     * @throws IOException
     */
    @Override
    protected synchronized EthBlock.Block pollLatestBlock() throws IOException {
        return fromMultiRPC();
    }

    private EthBlock.Block fromMultiRPC() throws IOException {
        for (Map.Entry<Web3j,String> web3jEntry : rpcServices.entrySet()) {
            try {
                long blockNumber = web3jEntry.getKey().ethBlockNumber().send().getBlockNumber().longValue(); // Latest block
                return web3jEntry.getKey().ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), true).send().getBlock();
            } catch (Exception e) {
                log.error("Couldn't connect with " + web3jEntry.getValue() + "! Is a valid API key defined in chain_infra_api_url.properties?" + System.lineSeparator() + ExceptionUtils.getStackTrace(e));
            }
        }
        log.error("Couldn't connect with any RPC service!");
       return null;
    }

    @Override
    protected Price pollLatestPricing() {
        return new Price(null, EPriceUnit.USD);
    }

    @Override
    public void cleanup() {
        rpcServices.keySet().forEach(Web3j::shutdown);
    }
}