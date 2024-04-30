package com.chain.chainscrape;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
@Component
public class Provider {
    private String INFURA_URL;
    private String ALCHEMY_URL;

    @PostConstruct
    private void readProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("chain_infra_api_url.properties");

            // Load the properties file
            prop.load(input);

            // Get property values
            INFURA_URL = prop.getProperty("eth_node_infra_provider_key");
            ALCHEMY_URL = prop.getProperty("alchemy_node_infra_provider_key");

            // Print property values (optional)
            System.out.println("Infura URL: " + INFURA_URL);
            System.out.println("Alchemy URL: " + ALCHEMY_URL);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}