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

@Service
public class FXRateService {
    public class fxRate {
        Double rate;
        java.util.Date lastUpdatedTime;

        public fxRate(Double rate) {
            this.rate = rate;
            this.lastUpdatedTime = new Date();
        }
    }

    Map<String, fxRate> fxRateCache = new HashMap<>();
    static final String RELAY_CURRENCY = "CAD";
    static final String FX_RATE_QUERY_URL_PREFIX = "ADD_HERE";
    static final String FX_RATE_QUERY_URL_SUFFIX = "ADD_HERE";

    public Double downloadRate(FXQuery q) {
        //fetch rate from Bank of Canada API
        String seriesName = "FX" + q.originCode + q.destinationCode;
        String url = FX_RATE_QUERY_URL_PREFIX + seriesName.replace("\"", "") + FX_RATE_QUERY_URL_SUFFIX;
        RestTemplate rt = new RestTemplate();
        Double rate = -1.0;
        try {
            try {
                String result = rt.getForObject(url, String.class);
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(result);
                rate = je.getAsJsonObject().getAsJsonArray("observations").get(0).getAsJsonObject().getAsJsonObject(seriesName).get("v").getAsDouble();
            } catch (HttpClientErrorException ex) { // if the direct FX rate is not found, use CAD as the relay
                System.out.println(ex.getStatusCode());
                System.out.println(ex.getResponseBodyAsString());
                FXQuery q1 = new FXQuery(q.originCode, RELAY_CURRENCY);
                FXQuery q2 = new FXQuery(RELAY_CURRENCY, q.destinationCode);
                rate = getRate(q1) * getRate(q2);
            }
        } catch (Exception ex) {
            System.out.println("FX rate not available: " + q.originCode + "->" + q.destinationCode + " " + ex);
        }
        fxRate r = new fxRate(rate);
        fxRateCache.put(q.getPairIden(), r);
        return rate;
    }

    public Double getRate(FXQuery q) {
        String cp = q.getPairIden();
        Double rate = -1.0;
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
