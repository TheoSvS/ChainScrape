package com.chain.chainscrape;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Utils {


    /**
     * Takes an Ethereum "decimal" (BigInteger representation multiplied by 10^x where x are the decimal places) and converts to BigDecimal
     *
     * @param ethDecRepresentation The Ethereum decimal represented as a BigInteger
     * @param ethDecimals          The decimal places represented in the BigInteger
     * @param outputDecimals       The decimal places of the output BigDecimal
     * @return The output BigDecimal
     */
    public static BigDecimal ethDecToBigDecimal(BigInteger ethDecRepresentation, int ethDecimals, int outputDecimals) {
        return new BigDecimal(ethDecRepresentation).divide(BigDecimal.valueOf(Math.pow(10, ethDecimals)), outputDecimals, RoundingMode.HALF_UP);
    }

    /** Checks if an input contains any of the substrings
     * @param input The string to check
     * @param toMatch The strings to match
     * @return True if the input contains any of the substrings
     */
    public static boolean containsAny(String input, String... toMatch){
        for(String s : toMatch){
            if(input.contains(s)){
                return true;
            }
        }
        return false;
    }
}
