package com.chain.chainscrape;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Provider {
    static {
        readProperties();
    }
    private static String  INFURA_URL ;

    private static synchronized void readProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("chain_infra_api_url.properties");

            // Load the properties file
            prop.load(input);

            // Get property values
            INFURA_URL = prop.getProperty("eth_node_infra_provider_key");

            // Print property values
            System.out.println("Infura URL: " + INFURA_URL);
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

    public static String getInfuraURL(){
        return INFURA_URL;
    }
}
