package com.chain.chainscrape.controllers;

import com.chain.chainscrape.services.BlockDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.web3j.protocol.core.methods.response.EthBlock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Controller
public class MainPageController {
    //wired from application.properties
    @Value("${appTitle}")
    String appTitle;
    private final BlockDataService blockDataService;

    public MainPageController(BlockDataService blockDataService) {
        this.blockDataService = blockDataService;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        EthBlock.Block latestBlock = blockDataService.getLastRetrievedBlockData();
        long timestamp = latestBlock.getTimestamp().longValue();
        String dateReadable = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()).toString();

        model.addAttribute("homePageTitle", appTitle);
        model.addAttribute("blockNum", latestBlock.getNumber());
        model.addAttribute("blockHash", latestBlock.getHash());
        model.addAttribute("blockTime", timestamp + " (" + dateReadable + ")");
        model.addAttribute("blockMiner", latestBlock.getMiner());
        model.addAttribute("blockTrans", latestBlock.getTransactions().size());
        double gasFeeGwei = latestBlock.getBaseFeePerGas().doubleValue() / Math.pow(10.0, 9.0);
        double roundedGasFeeGwei = Math.round(gasFeeGwei * 10.0) / 10.0;
        model.addAttribute("gasGwei", roundedGasFeeGwei);

        return "homePage";
    }
}