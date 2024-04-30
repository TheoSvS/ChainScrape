package com.chain.chainscrape.services;


import com.chain.chainscrape.Provider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;

@Component
@Slf4j
public class Scrapper implements Runnable {
    private final Web3j web3jInfura;
    private final Web3j web3jAlchemy;

    private volatile EthBlock.Block latestCachedBlock;
    protected Scrapper(Provider provider) {
        web3jInfura = Web3j.build(new HttpService(provider.getINFURA_URL()));
        web3jAlchemy = Web3j.build(new HttpService(provider.getALCHEMY_URL()));
    }

    @Override
    public void run() {
        try {
            latestCachedBlock = retrieveLatestBlockData();
            if (latestCachedBlock != null) {
                log.info("Current Ethereum Block Number: " + latestCachedBlock.getNumber());
            } else {
                log.warn("Block couldn't be retrieved ");
            }
        } catch (Error | Exception e) { //intercept all exceptions, if a runnable within executor service fails, it won't print in default console by default
            log.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    /** Retrieves latest block from the blockchain.
     * @return the block
     * @throws IOException
     */
    protected synchronized EthBlock.Block retrieveLatestBlockData() throws IOException {
        long blockNumber = web3jInfura.ethBlockNumber().send().getBlockNumber().longValue(); // Latest block
        return web3jInfura.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), true).send().getBlock();
    }

    /** Return the latestCachedBlock in order to avoid unnecessary calls to the blockchain.
     * New blocks are generated about once every 10 seconds, facilitated by the executor service.
     * @return the latestCachedBlock
     */
    protected EthBlock.Block retrieveLatestCachedData(){
        try {
            return latestCachedBlock != null ? latestCachedBlock : this.retrieveLatestBlockData();
        } catch (Error | IOException e) {
            log.error("Block couldn't be retrieved! Did you add your key in chain_infra_api_url.properties?" + System.lineSeparator() + ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

}