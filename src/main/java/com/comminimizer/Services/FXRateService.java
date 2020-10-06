package com.comminimizer.Services;

import com.comminimizer.Query.FXQuery;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.comminimizer.ConfigProperties.FX_RATE_QUERY_URL_PREFIX;
import static com.comminimizer.ConfigProperties.FX_RATE_QUERY_URL_SUFFIX;

@Service
public class FXRateService {
    static public class fxRate {
        Double rate;
        java.util.Date lastUpdatedTime;

        /**
         * Constructor for {@code fxRate}
         * @param rate numeric part of the FX rate
         */
        public fxRate(Double rate) {
            this.rate = rate;
            this.lastUpdatedTime = new Date();
        }
    }

    Map<String, fxRate> fxRateCache = new HashMap<>();
    static final String RELAY_CURRENCY = "CAD";

    /**
     * It downloads the FX rate from Bank of Canada API. It may use CAD as a relay if needed.
     * It stores the rate in the cache.
     * @param q {@code FXQuery} object designating the original currency and destination currency
     * @return the FX rate to convert original currency to the destination currency
     */
    public Double downloadRate(FXQuery q) {
        //fetch rate from Bank of Canada API
        String seriesName = "FX" + q.originCode + q.destinationCode;
        String url = FX_RATE_QUERY_URL_PREFIX + seriesName.replace("\"", "") + FX_RATE_QUERY_URL_SUFFIX;
        RestTemplate rt = new RestTemplate();
        double rate = -1.0;
        try {
            try {
                String result = rt.getForObject(url, String.class);
                if(result == null) {
                    throw new Exception("FX Rate Fetched Null.");
                }
                JsonElement je = JsonParser.parseString(result);
                rate = je.getAsJsonObject().getAsJsonArray("observations").get(0).getAsJsonObject().getAsJsonObject(seriesName).get("v").getAsDouble();
            } catch (HttpClientErrorException ex) { // if the direct FX rate is not found, use CAD as the relay
                ComMinimizer.log.warn("FX Rate Fetching Failed. Status: " + ex.getStatusCode());
                ComMinimizer.log.warn("Response Body: " + ex.getResponseBodyAsString());
                FXQuery q1 = new FXQuery(q.originCode, RELAY_CURRENCY);
                FXQuery q2 = new FXQuery(RELAY_CURRENCY, q.destinationCode);
                rate = getRate(q1) * getRate(q2);
            }
        } catch (Exception ex) {
            ComMinimizer.log.warn("FX rate not available: " + q.originCode + "->" + q.destinationCode + " " + ex);
        }
        fxRate r = new fxRate(rate);
        fxRateCache.put(q.getPairIden(), r);
        return rate;
    }

    /**
     * It fetches the FX rate from cache (if there is one and not expired), or downloads
     * the rate.
     * @param q {@code FXQuery} object designating the original currency and destination currency
     * @return the FX rate to convert original currency to the destination currency
     */
    public Double getRate(FXQuery q) {
        String cp = q.getPairIden();
        Double rate;
        if (q.originCode.equals(q.destinationCode)) {
            rate = 1.0;
            return rate;
        }
        if(fxRateCache.containsKey(cp)){
            Date current = new Date();
            Calendar c = Calendar.getInstance();
            fxRate r = fxRateCache.get(cp);
            c.setTime(r.lastUpdatedTime);
            c.add(Calendar.DATE, 1);
            Date nextUpdate = c.getTime();
            if(current.before(nextUpdate)) {
                rate = r.rate;
            } else {
                rate = downloadRate(q);
            }
        } else {
            rate = downloadRate(q);
        }
        return rate;
    }
}
