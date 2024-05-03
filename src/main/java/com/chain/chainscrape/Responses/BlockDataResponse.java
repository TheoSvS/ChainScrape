package com.chain.chainscrape.Responses;

import com.chain.chainscrape.model.EthData;
import lombok.Data;
import org.web3j.protocol.core.methods.response.EthBlock;

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
        String dateReadable = LocalDateTime.ofInstant(Instant.ofEpochSecond(ethData.block().getTimestamp().longValue()), ZoneId.systemDefault()).toString();
        this.blockNum = ethData.block().getNumber();
        this.blockHash = ethData.block().getHash();
        this.time = ethData.block().getTimestamp() + " (" + dateReadable + ")";
        this. miner = ethData.block().getMiner();
        this.transactions = ethData.block().getTransactions().size();
        this.usdPrice = ethData.usdPrice();
    }
}
