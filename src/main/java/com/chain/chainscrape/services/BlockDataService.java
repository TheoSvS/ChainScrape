package com.chain.chainscrape.services;


import java.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthBlock;

@Slf4j
@Component
public class BlockDataService {
    private final Scrapper scrapperRunnable;

    public BlockDataService(Scrapper scrapperRunnable) {
        this.scrapperRunnable = scrapperRunnable;
        monitor();
    }

    public void monitor() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(scrapperRunnable, 0, 10, TimeUnit.SECONDS);
    }

    /**
     * To not exceed our free INFURA Api quota, return the last stored block, and allow the executor service to update it only once every 10 seconds (ethereum new block time)
     *
     * @return
     */
    public  EthBlock.Block getLastRetrievedBlockData() {
        try {
            return scrapperRunnable.retrieveLatestCachedData();
        } catch (Error e) {
            log.error("Block couldn't be retrieved! Did you add your key in chain_infra_api_url.properties?" + System.lineSeparator() + ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

}
