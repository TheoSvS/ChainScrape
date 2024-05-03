package com.chain.chainscrape;

import com.chain.chainscrape.controllers.EthDataViewController;
import com.chain.chainscrape.model.EthData;
import com.chain.chainscrape.services.BlockDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class EthDataViewControllerTest {

    @Mock
    private BlockDataService mockBlockDataService = Mockito.mock(BlockDataService.class);

    @InjectMocks
    private EthDataViewController ethDataViewController;

    @Test
    public void testHomePage_WithBlockData() {
        // Mock Block Data
        EthBlock.Block mockBlock = new EthBlock.Block();
        mockBlock.setNumber("12345");
        mockBlock.setHash("0x1234567890abcdef");
        mockBlock.setTimestamp(BigInteger.valueOf(1656579200).toString()); // Assuming a timestamp value
        mockBlock.setMiner("0xminerAddress");
        mockBlock.setTransactions(new ArrayList<>());
        mockBlock.getTransactions().add(new EthBlock.TransactionHash("txHash1"));
        mockBlock.getTransactions().add(new EthBlock.TransactionHash("txHash2"));
        mockBlock.setBaseFeePerGas(BigInteger.valueOf(1000000000).toString()); // 1000000000 wei

        // Mock BlockDataService behavior
        Mockito.when(mockBlockDataService.getLatestEthData()).thenReturn(new EthData(mockBlock, BigDecimal.valueOf(9999.99)));

        // Execute the controller method
        Model model = new ExtendedModelMap();
        String viewName = ethDataViewController.homePage(model);

        // Assertions
        assertEquals("homePage", viewName);
        Map<String, Object> modelAttributes = model.asMap();
        assertEquals(BigInteger.valueOf(12345), modelAttributes.get("blockNum"));
        assertEquals("0x1234567890abcdef", modelAttributes.get("blockHash"));
        assertTrue(((String) modelAttributes.get("blockTime")).contains("1656579200")); // Check for timestamp presence
        assertEquals("0xminerAddress", modelAttributes.get("blockMiner"));
        assertEquals(2, modelAttributes.get("blockTrans"));
        assertEquals(1.0, modelAttributes.get("gasGwei")); // 1000000000 wei = 1 Gwei
        assertEquals(BigDecimal.valueOf(9999.99), modelAttributes.get("usdPrice")); // 1000000000 wei = 1 Gwei
    }
}