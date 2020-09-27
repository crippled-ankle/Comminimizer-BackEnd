package com.comminimizer.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceEndpoint {

    @Autowired
    private CommissionSearchService css;

    @Autowired
    private InstrumentSearchService iss;

    @CrossOrigin(origins = "http://comminimizer.com")
    @PostMapping("/search")
    public String searchCom(@RequestBody String requestBody){
        return css.queryCommissionDB(requestBody);
    }

    @CrossOrigin(origins = "http://comminimizer.com")
    @GetMapping("/search-instrument/{iden}")
    public String searchInstrument(@PathVariable String iden){
        return iss.searchInstrument(iden);
    }
}
