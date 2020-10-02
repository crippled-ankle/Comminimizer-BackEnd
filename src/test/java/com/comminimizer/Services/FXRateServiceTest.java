package com.comminimizer.Services;

import com.comminimizer.Query.FXQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FXRateServiceTest {
    @Autowired
    FXRateService fxrs;

    @Test
    void testSameCode() throws Exception {
        FXQuery q = new FXQuery("CAD", "CAD");
        assert fxrs.getRate(q).equals(1.0) : "FX rate not correct (Same Code)";
    }

    @Test
    void testDifferentCodes() throws Exception {
        FXQuery q = new FXQuery("USD", "CAD");
        assert fxrs.getRate(q) > 0.0 : "FX rate not correct (Different Codes)";
    }

    @Test
    void testRelayCurrency() throws Exception {
        FXQuery q = new FXQuery("SGD", "USD");
        assert fxrs.getRate(q) > 0.0 : "FX rate not correct (Using Relay Currency)";
    }
}
