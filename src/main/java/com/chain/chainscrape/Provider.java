package com.chain.chainscrape;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
@Component
@Slf4j
public class Provider {
    private String INFURA_URL;
    private String ALCHEMY_URL;

    @PostConstruct
    private void readProperties() {
        Properties prop = new Properties();

        try (InputStream input = new FileInputStream("chain_infra_api_url.properties")) {
            // Load the properties file
            prop.load(input);

            // Get property values
            INFURA_URL = prop.getProperty("eth_node_infra_provider_key");
            ALCHEMY_URL = prop.getProperty("alchemy_node_infra_provider_key");

            // Print property values (optional)
            System.out.println("Infura URL: " + INFURA_URL);
            System.out.println("Alchemy URL: " + ALCHEMY_URL);
        } catch (IOException ex) {
            log.error("FAIL" + ex.getMessage(), ExceptionUtils.getStackTrace(ex));
        }
    }
}