package com.comminimizer.Services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.comminimizer.ConfigProperties.*;

@Service
public class InstrumentSearchService {

    public String searchInstrument(String iden) {
        RestTemplate rt = new RestTemplate();
        String url = INSTRUMENT_SEARCH_URL_PREFIX + iden;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HTTP_HEADER_API_HOST_NAME, HTTP_HEADER_API_HOST);
        headers.set(HTTP_HEADER_API_KEY_NAME, HTTP_HEADER_API_KEY);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = rt.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}
