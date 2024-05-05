package com.chain.chainscrape.Responses;

import com.chain.chainscrape.model.data.EthData;
import lombok.Data;

import java.math.BigDecimal;
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
    private BigDecimal usdPrice;

    public BlockDataResponse(String msg, EthData ethData) {
        super(msg);
        String dateReadable = LocalDateTime.ofInstant(Instant.ofEpochSecond(ethData.getBlock().getTimestamp().longValue()), ZoneId.systemDefault()).toString();
        this.blockNum = ethData.getBlock().getNumber();
        this.blockHash = ethData.getBlock().getHash();
        this.time = ethData.getBlock().getTimestamp() + " (" + dateReadable + ")";
        this. miner = ethData.getBlock().getMiner();
        this.transactions = ethData.getBlock().getTransactions().size();
        this.usdPrice = ethData.getAssetPrice().price();
    }
}
