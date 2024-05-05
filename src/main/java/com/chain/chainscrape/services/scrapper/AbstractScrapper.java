package com.chain.chainscrape.services.scrapper;

import com.chain.chainscrape.model.data.ChainData;
import com.chain.chainscrape.model.Price;
import com.chain.chainscrape.services.BlockDataService;
import jakarta.annotation.PreDestroy;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public abstract class AbstractScrapper<C extends ChainData ,B> implements Runnable {
    protected volatile C latestCachedData;
    /**
     * Retrieves latest block from a blockchain.
     *
     * @return the block or data
     * @throws IOException if there's an error retrieving the data
     */
    protected abstract B pollLatestBlock(Web3j web3jRPCservice) throws IOException;

    /**
     * @return return latest trading price of a blockchain's native asset
     * @throws IOException
     */
    protected abstract Price pollLatestPricing(Web3j web3j) throws InterruptedException, ExecutionException;

    /** Return the latestCachedBlock in order to avoid unnecessary calls to the blockchain.
     * Calls for new blocks are facilitated by the executor service of {@link BlockDataService}.
     * @return the latestCachedBlock
     */
    public abstract C retrieveLatestCachedData();

    /**
     * Cleans up any resources used by the scrapper.
     */
    @PreDestroy
    public abstract void cleanup() ;
}
