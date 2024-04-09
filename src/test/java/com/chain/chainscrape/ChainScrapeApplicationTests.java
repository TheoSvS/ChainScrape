package com.chain.chainscrape;

import com.chain.chainscrape.services.BlockDataService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChainScrapeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testScrape() throws Exception {
        new BlockDataService().monitor();
        Thread.sleep(5000); //allow enough time for executor service thread to run
    }
}
