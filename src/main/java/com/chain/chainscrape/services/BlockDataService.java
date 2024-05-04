package com.chain.chainscrape.services;


import java.util.concurrent.*;

import com.chain.chainscrape.Provider;
import com.chain.chainscrape.model.EthData;
import com.chain.chainscrape.services.impl.EthScrapper;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthBlock;

/**
 * Service that manages the blockchain data retrieval through the respective IScrappers
 */
@Slf4j
@Component
public class BlockDataService {
    private final AbstractScrapper<EthData, EthBlock.Block> scrapperRunnable;
    private  ScheduledExecutorService scheduledExecutorService;

    public BlockDataService(Provider provider) {
        this.scrapperRunnable = new EthScrapper(provider);
        monitor();
    }

    public void monitor() {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
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
