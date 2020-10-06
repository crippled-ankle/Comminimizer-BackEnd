package com.comminimizer.Instrument;

import com.comminimizer.Services.ComMinimizer;
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

    /**
     * Constructor for {@code Instrument}
     * @param iden instrument identifier
     * @param idenType instrument identifier type
     */
    public Instrument( String iden, Integer idenType ) {
        this.iden = iden;
        this.idenType = idenType;
    }

    /**
     * It sets attributes of this instrument based on the supplied values from Yahoo API
     */
    public void setAttrFromSearch() {
        RestTemplate rt = new RestTemplate();
        String url = INSTRUMENT_QUOTE_URL_PREFIX + this.iden.replace("\"", "");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HTTP_HEADER_API_HOST_NAME, HTTP_HEADER_API_HOST);
        headers.set(HTTP_HEADER_API_KEY_NAME, HTTP_HEADER_API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(url, HttpMethod.GET, entity, String.class);
        try {
            JsonObject quote = JsonParser.parseString(response.getBody()).getAsJsonObject().getAsJsonObject("quoteResponse").getAsJsonArray("result").get(0).getAsJsonObject();
            instrumentType = 1; // TODO set proper instrument type
            currency = quote.get("currency").toString().replace("\"", "");
            referencePrice = Double.parseDouble(quote.get("regularMarketPrice").toString().replace("\"", ""));
            contractSize = 1.0; // TODO set proper contract size
        } catch (Exception ex) {
            ComMinimizer.log.warn("Reference price not available: " + iden + " " + ex);
        }
    }
}
