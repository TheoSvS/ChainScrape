package com.chain.chainscrape.model;

import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;

public record EthData(EthBlock.Block block, BigDecimal usdPrice) {
    public EthData(EthBlock.Block block) {
        this(block,null);
    }
}
