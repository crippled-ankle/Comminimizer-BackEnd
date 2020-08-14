package com.comminimizer.Query;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.web.client.RestTemplate;

public class FXQuery {
    String originCode = "";
    String destinationCode = "";
    Double rate = -1.0;

    static final String FX_RATE_QUERY_URL_PREFIX = "ADD_HERE";
    static final String FX_RATE_QUERY_URL_SUFFIX = "ADD_HERE";

    public FXQuery(String originCode, String destinationCode) {
        originCode = originCode;
        destinationCode = destinationCode;
    }

    //TODO Add rate cache in DB
    public Double getRate() {
        if (originCode.equals(destinationCode)) {
            rate = 1.0;
            return rate;
        }
        //fetch rate from Bank of Canada API
        String seriesName = "FX" + originCode + destinationCode;
        String url = FX_RATE_QUERY_URL_PREFIX + seriesName + FX_RATE_QUERY_URL_SUFFIX;
        RestTemplate rt = new RestTemplate();
        Double rate = -1.0;
        try {
            String result = rt.getForObject(url, String.class);
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(result);
            rate = je.getAsJsonObject().getAsJsonArray("observations").get(0).getAsJsonObject().getAsJsonObject(seriesName).get("v").getAsDouble();
        } catch (Exception ex) {
            System.out.println("FX rate not available: " + originCode + "->" + destinationCode + " " + ex);
        }
        return rate;
    }
}
