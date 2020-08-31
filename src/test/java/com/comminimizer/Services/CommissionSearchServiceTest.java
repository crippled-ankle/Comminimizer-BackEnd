package com.comminimizer.Services;

import com.comminimizer.Query.CommissionQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommissionSearchServiceTest {
    @Autowired
    private CommissionSearchService ss;

    @Test
    void testCalculateTradeValue() throws Exception {
        Double t1Res = ss.calculateTradeValue(2.0, 50.0);
        assert t1Res == 100.0 : "Wrong Trade Value Expected: 100.0 Got: " + t1Res;
    }

    @Test
    void testCalculateCom() throws Exception {
        CommissionQuery test1 = new CommissionQuery(" {\"Instr\": \"DOL.TRT\", \"InstrType\": 1, \"Market\": \"Toronto\", \"Currency\": \"CAD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t1Res = ss.calculateCom(test1,1, 6.88, "CAD", 6.88, 6.88, 5000.0, 1, 0, 0.0).origin;
        assert t1Res == 6.88 : "Expected: 6.88 Got: " + t1Res;

        CommissionQuery test2 = new CommissionQuery(" {\"Instr\": \"TSLA\", \"InstrType\": 1, \"Market\": \"United States\", \"Currency\": \"USD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t2Res = ss.calculateCom(test2, 2, 0.005, "USD", 1.0, 0.01, 170000.0, 2, 0, 0.0).origin;
        assert t2Res == 1.0 : "Expected: 1.0 Got: " + t2Res;

        CommissionQuery test3 = new CommissionQuery(" {\"Instr\": \"DOL.TRT\", \"InstrType\": 1, \"Market\": \"Toronto\", \"Currency\": \"CAD\", \"Quantity\": 3000, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t3Res = ss.calculateCom(test3, 3, 0.01, "CAD", 19.96, 1000000.0, 150000.0, 1, 2001, 19.5).origin;
        assert t3Res == 29.5 : "Expected: 29.5 Got: " + t3Res;

        CommissionQuery test4 = new CommissionQuery(" {\"Instr\": \"TSLA\", \"InstrType\": 1, \"Market\": \"United States\", \"Currency\": \"USD\", \"Quantity\": 10000, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t4Res = ss.calculateCom(test4, 2, 0.005, "USD", 1.0, 0.01, 6000.0, 2, 0, 0.0).origin;
        assert t4Res == 50.0 : "Expected: 50.0 Got: " + t2Res;

    }
}
