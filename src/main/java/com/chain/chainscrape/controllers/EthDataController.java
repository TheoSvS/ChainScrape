package com.chain.chainscrape.controllers;

import com.chain.chainscrape.Responses.BlockDataResponse;
import com.chain.chainscrape.model.EthData;
import com.chain.chainscrape.services.BlockDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin(origins = " * ", allowedHeaders = " * ")
@RequestMapping(value="/api/v1")
public class EthDataController {
    private final BlockDataService blockDataService;

    public EthDataController(BlockDataService blockDataService) {
        this.blockDataService = blockDataService;
    }

    @GetMapping(value="/blockdata")
    public ResponseEntity<BlockDataResponse> getBlockData(){
        EthData ethData = blockDataService.getLatestEthData();
        if(ethData!=null){
            return ResponseEntity.ok(new BlockDataResponse("Block retrieved!",  ethData));
        }
        return ResponseEntity.notFound().build();
    }
}
