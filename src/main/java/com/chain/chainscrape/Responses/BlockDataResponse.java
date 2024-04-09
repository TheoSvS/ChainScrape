package com.chain.chainscrape.Responses;

import lombok.Data;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class BlockDataResponse extends Response{

    private BigInteger blockNum;
    private String blockHash;
    private String time;
    private String miner;
    private int transactions;

    public BlockDataResponse(String msg, EthBlock.Block block) {
        super(msg);
        String dateReadable = LocalDateTime.ofInstant(Instant.ofEpochSecond(block.getTimestamp().longValue()), ZoneId.systemDefault()).toString();
        this.blockNum = block.getNumber();
        this.blockHash = block.getHash();
        this.time = block.getTimestamp() + " (" + dateReadable + ")";
        this. miner = block.getMiner();
        this.transactions = block.getTransactions().size();

    }
}
