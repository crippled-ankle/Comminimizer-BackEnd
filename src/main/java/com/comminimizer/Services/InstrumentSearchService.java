package com.comminimizer.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InstrumentSearchService {

    static final String INSTRUMENT_SEARCH_URL_PREFIX = "ADD_HERE";
    static final String INSTRUMENT_SEARCH_URL_SUFFIX = "ADD_HERE";

    public String searchInstrument(String iden) {
        RestTemplate rt = new RestTemplate();
        String url = INSTRUMENT_SEARCH_URL_PREFIX + iden + INSTRUMENT_SEARCH_URL_SUFFIX;
        return rt.getForObject(url, String.class);
    }
}
