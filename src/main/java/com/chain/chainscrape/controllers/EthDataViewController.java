package com.chain.chainscrape.controllers;

import com.chain.chainscrape.model.EthData;
import com.chain.chainscrape.services.BlockDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Controller
public class EthDataViewController {
    //wired from application.properties
    @Value("${appTitle}")
    String appTitle;
    private final BlockDataService blockDataService;

    public EthDataViewController(BlockDataService blockDataService) {
        this.blockDataService = blockDataService;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        EthData ethData = blockDataService.getLatestEthData();
        long timestamp = ethData.block().getTimestamp().longValue();
        String dateReadable = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()).toString();

        model.addAttribute("homePageTitle", appTitle);
        model.addAttribute("blockNum", ethData.block().getNumber());
        model.addAttribute("blockHash", ethData.block().getHash());
        model.addAttribute("blockTime", timestamp + " (" + dateReadable + ")");
        model.addAttribute("blockMiner", ethData.block().getMiner());
        model.addAttribute("blockTrans", ethData.block().getTransactions().size());
        double gasFeeGwei = ethData.block().getBaseFeePerGas().doubleValue() / Math.pow(10.0, 9.0);
        double roundedGasFeeGwei = Math.round(gasFeeGwei * 10.0) / 10.0;
        model.addAttribute("gasGwei", roundedGasFeeGwei);
        model.addAttribute("usdPrice", ethData.usdPrice());

        return "homePage";
    }
}