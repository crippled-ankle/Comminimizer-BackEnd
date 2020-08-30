package com.comminimizer.Query;

public class FXQuery {
    public String originCode;
    public String destinationCode;

    public FXQuery(String originCode, String destinationCode) {
        this.originCode = originCode;
        this.destinationCode = destinationCode;
    }

    public String getPairIden() {
        return originCode + "-" + destinationCode;
    }
}
