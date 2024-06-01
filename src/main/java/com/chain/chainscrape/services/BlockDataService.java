package com.chain.chainscrape.services;


import java.util.concurrent.*;

import com.chain.chainscrape.RPCProvider;
import com.chain.chainscrape.model.data.EthData;
import com.chain.chainscrape.services.scrapper.AbstractScrapper;
import com.chain.chainscrape.services.scrapper.EthScrapper;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;

/**
 * Service that manages the blockchain data retrieval through the respective IScrappers
 */
@Slf4j
@Service
public class BlockDataService {
    private final AbstractScrapper<EthData, EthBlock.Block> scrapperRunnable;
    private final ScheduledExecutorService scheduledExecutorService;

    public BlockDataService(RPCProvider RPCProvider) {
        this.scrapperRunnable = new EthScrapper(RPCProvider);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        monitor();
    }

    public void monitor() {
            scheduledExecutorService.scheduleAtFixedRate(scrapperRunnable, 0, 10, TimeUnit.SECONDS);
    }


    /**
     * To not exceed our free RPC Api quota, return the last stored block, and allow the executor service to update it only once every 10 seconds (ethereum new block time)
     *
     * @return
     */
    public EthData getLatestEthData() {
        try {
            return scrapperRunnable.retrieveLatestCachedData();
        } catch (Error e) {
            log.error("Block couldn't be retrieved! Did you add your key in chain_infra_api_url.properties?" + System.lineSeparator() + ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    /**
     * Shutdown the executorservice
     */
    @PreDestroy
    public void cleanup() {
        scheduledExecutorService.shutdown();
    }

}
