package com.comminimizer.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceEndpoint {

    @Autowired
    private CommissionSearchService ss;

    @Autowired
    private InstrumentSearchService iss;

    @PostMapping("/search")
    public String searchCom(@RequestBody String requestBody){
        return ss.queryCommissionDB(requestBody);
    }

    @GetMapping("/search-instrument/{iden}")
    public String searchInstrument(@PathVariable String iden){
        return iss.searchInstrument(iden);
    }
}
