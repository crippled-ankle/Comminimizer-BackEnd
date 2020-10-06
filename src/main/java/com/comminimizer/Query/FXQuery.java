package com.comminimizer.Query;

public class FXQuery {
    public String originCode;
    public String destinationCode;

    /**
     * Constructor for {@code FXQuery} object, consisting
     * of origin currency code and destination currency code
     * @param originCode currency code of the origin
     * @param destinationCode currency code of the destination
     */
    public FXQuery(String originCode, String destinationCode) {
        this.originCode = originCode;
        this.destinationCode = destinationCode;
    }

    /**
     * It constructs the currency pair identifier used in FX rate caching
     * @return a currency pair identifier
     */
    public String getPairIden() {
        return originCode + "-" + destinationCode;
    }
}
