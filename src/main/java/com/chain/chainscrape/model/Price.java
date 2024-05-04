package com.chain.chainscrape.model;

import java.math.BigDecimal;

/** The price of an asset
 * @param price the price of the asset
 * @param unit the unit of pricing
 */
public record Price(BigDecimal price,EPriceUnit unit) {
}
