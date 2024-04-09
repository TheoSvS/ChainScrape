package com.chain.chainscrape.controllers;

import com.chain.chainscrape.Responses.BlockDataResponse;
import com.chain.chainscrape.services.BlockDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.response.EthBlock;

@RestController
@Slf4j
@CrossOrigin(origins = " * ", allowedHeaders = " * ")
@RequestMapping(value="/api/v1")
public class BlockDataProviderController {

    @GetMapping(value="/getblockdata")
    public ResponseEntity<BlockDataResponse> getBlockData(){
        EthBlock.Block block = BlockDataService.getLastRetrievedBlock();
        if(block!=null){
            return ResponseEntity.ok(new BlockDataResponse("Block retrieved!",  block));
        }
        return ResponseEntity.notFound().build();
    }
}
