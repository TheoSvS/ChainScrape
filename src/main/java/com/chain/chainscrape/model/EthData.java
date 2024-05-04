package com.chain.chainscrape.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.protocol.core.methods.response.EthBlock;
import java.math.BigDecimal;

/**
 * Holds heartbeat information for ethereum e.g. block data, price etc
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class EthData extends ChainData{
    private EthBlock.Block block;
    private Price assetPrice;
    public EthData(EthBlock.Block block, Price assetPrice) {
        this.block = block;
        this.assetPrice = assetPrice;
    }

    public EthData(EthBlock.Block block) {
        this(block,null);
    }
}
