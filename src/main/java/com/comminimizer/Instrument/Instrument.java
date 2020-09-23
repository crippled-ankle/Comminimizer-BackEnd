package com.comminimizer.Instrument;

import com.google.gson.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.comminimizer.ConfigProperties.*;

public class Instrument {
    public Integer instrumentType = 1; // for now, it supports stocks only
    public String iden = "";
    public Integer idenType = 1; // for now, it supports "ticker root" only
    public String region;
    public String currency;
    public Double referencePrice = -1.0;
    public Double contractSize = 1.0;

    public Instrument( String iden, Integer idenType ) {
        this.iden = iden;
        this.idenType = idenType;
    }

    public void setAttrFromSearch() {
        // TODO encapsulate configuration
        RestTemplate rt = new RestTemplate();
        String url = INSTRUMENT_QUOTE_URL_PREFIX + this.iden.replace("\"", "");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HTTP_HEADER_API_HOST_NAME, HTTP_HEADER_API_HOST);
        headers.set(HTTP_HEADER_API_KEY_NAME, HTTP_HEADER_API_KEY);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = rt.exchange(url, HttpMethod.GET, entity, String.class);
        try {
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(response.getBody());
            JsonObject quote = je.getAsJsonObject().getAsJsonObject("quoteResponse").getAsJsonArray("result").get(0).getAsJsonObject();
            instrumentType = 1; // TODO set proper instrument type
            currency = quote.get("currency").toString().replace("\"", "");
            referencePrice = Double.parseDouble(quote.get("regularMarketPrice").toString().replace("\"", ""));
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
