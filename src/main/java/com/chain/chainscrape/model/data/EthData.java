package com.chain.chainscrape.model.data;

import com.chain.chainscrape.Utils;
import com.chain.chainscrape.model.Price;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Holds heartbeat information for ethereum e.g. block data, price etc
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class EthData implements IChainData{
    private EthBlock.Block block;
    private Price assetPrice;
    private BigDecimal totalEthWithdrawals;
    private BigDecimal totalEthWithdrawalsInSelectedUnit; //the total Eth withdrawals converted in the selected asset

    public EthData(EthBlock.Block block, Price assetPrice) {
        this.block = block;
        this.assetPrice = assetPrice;
        BigInteger ethDec = block.getWithdrawals().stream().map(EthBlock.Withdrawal::getAmount).reduce(BigInteger.ZERO, BigInteger::add);
        this.totalEthWithdrawals = Utils.ethDecToBigDecimal(ethDec,9,9);
        this.totalEthWithdrawalsInSelectedUnit = assetPrice != null ?
                assetPrice.price().multiply(totalEthWithdrawals).setScale(2, RoundingMode.HALF_UP) : null;
    }

    public EthData(EthBlock.Block block) {
        this(block,null);
    }
}
