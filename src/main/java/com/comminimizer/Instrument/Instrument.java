package com.comminimizer.Instrument;

import com.google.gson.*;
import org.springframework.web.client.RestTemplate;

public class Instrument {
    public Integer instrumentType = 1; // for now, it supports stocks only
    public String iden = "";
    public Integer idenType = 1; // for now, it supports "ticker root" only
    public String region;
    public String currency;
    public Double referencePrice = -1.0;
    public Double contractSize = 1.0;

    static final String INSTRUMENT_QUOTE_URL_PREFIX = "ADD_HERE";
    static final String INSTRUMENT_QUOTE_URL_SUFFIX = "ADD_HERE";

    public Instrument( String iden, Integer idenType ) {
        this.iden = iden;
        this.idenType = idenType;
    }

    public void setAttrFromSearch() {
        // TODO encapsulate configuration
        RestTemplate rt = new RestTemplate();
        // Alpha Vantage Quote
        String url = INSTRUMENT_QUOTE_URL_PREFIX + this.iden + INSTRUMENT_QUOTE_URL_SUFFIX;
        try {
            String result = rt.getForObject(url, String.class);
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(result);
            JsonObject globalQuote = je.getAsJsonObject().getAsJsonObject("Global Quote");
            instrumentType = 1; // TODO set proper instrument type
            //this.iden = realData.get("01. symbol").toString().replace("\"", "");
            //region = realData.get("4. region").toString().replace("\"", "");
            //currency = realData.get("8. currency").toString().replace("\"", "");
            referencePrice = Double.parseDouble(globalQuote.get("05. price").toString().replace("\"", ""));
            contractSize = 1.0; // TODO set proper contract size
        } catch (Exception ex) {
            System.out.println("Reference price not available: " + iden + " " + ex);
        }
    }

    public String jsonifyInstrument() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
