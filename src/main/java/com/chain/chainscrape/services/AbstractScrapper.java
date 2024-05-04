package com.chain.chainscrape.services;

import com.chain.chainscrape.model.ChainData;
import com.chain.chainscrape.model.Price;
import jakarta.annotation.PreDestroy;

import java.io.IOException;

public abstract class AbstractScrapper<C extends ChainData ,B> implements Runnable {
    protected volatile C latestCachedData;
    /**
     * Retrieves latest block from a blockchain.
     *
     * @return the block or data
     * @throws IOException if there's an error retrieving the data
     */
    protected abstract B pollLatestBlock() throws IOException;

    /**
     * @return return latest trading price of a blockchain's native asset
     * @throws IOException
     */
    protected abstract Price pollLatestPricing() throws IOException;

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
