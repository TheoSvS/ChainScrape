package com.chain.chainscrape.services;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.*;

import com.chain.chainscrape.Provider;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.EthBlock;

public class BlockDataService {
    private static Web3j web3j;
    private static EthBlock.Block block;

    static {
        init();
    }

    private static synchronized void init() {
        // Replace "YOUR_INFURA_URL" with your Infura node URL inside chain_infra_api_url.properties
        String INFURA_URL = Provider.getInfuraURL();

        // Connect to the Ethereum node using Web3j
        web3j = Web3j.build(new HttpService(INFURA_URL));
        monitor();
    }

    public static void monitor() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ScrapperRunnable(), 0, 10, TimeUnit.SECONDS);
    }

    /** To not exceed our free INFURA Api quota, return the last stored block, and allow the executor service to update it only once every 10 seconds (ethereum new block time)
     * @return
     */
    public static EthBlock.Block getLastRetrievedBlock() {
        try {
            return block!=null?block:retrieveLatestBlockFromBlockchain();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static EthBlock.Block retrieveLatestBlockFromBlockchain() throws IOException {
        long blockNumber = web3j.ethBlockNumber().send().getBlockNumber().longValue(); // Latest block
        block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), true).send().getBlock();
        return block;
    }

    public static class ScrapperRunnable implements Runnable {

        @Override
        public void run() {
            try {
               EthBlock.Block block = BlockDataService.retrieveLatestBlockFromBlockchain();
                if (block != null) {
                    System.out.println("Current Ethereum Block Number: " + block.getNumber());
                    System.out.println("Block Hash: " + block.getHash());
                    long timestamp = block.getTimestamp().longValue();
                    String dateReadable = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()).toString();
                    System.out.println("Timestamp: " + timestamp + " (" + dateReadable + ")");
                    System.out.println("Block produced by Miner: " + block.getMiner());
                    System.out.println("Number of Transactions: " + block.getTransactions().size());
                } else {
                    System.out.println("Block couldn't be found for number");
                }
            } catch (Exception e) { //intercept all exceptions, if a runnable within executor service fails, it won't print in default console by default
                e.printStackTrace();
            }
        }

    }
}
