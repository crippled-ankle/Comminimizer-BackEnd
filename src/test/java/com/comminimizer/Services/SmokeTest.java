package com.comminimizer.Services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
public class SmokeTest {
    @Autowired
    private CommissionSearchService ss;

    @Autowired
    private InstrumentSearchService iss;

    @Test
    void testSmokeTest() throws Exception {
        assert !Objects.isNull(ss) : "Object is NULL";
        assert !Objects.isNull(iss) : "Object is NULL";
    }
}
