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
    void testCalculateCom() throws Exception {
        // Case 1: Flat Rate
        CommissionQuery test1 = new CommissionQuery(" {\"Instr\": \"DOL.TO\", \"InstrType\": 1, \"Market\": \"TOR\", \"Currency\": \"CAD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t1Res = ss.calculateCom(test1,1, 6.88, "CAD", 6.88, 6.88,1, 0, 0.0, 0.0).origin;
        assert t1Res.equals(6.88) : "Expected: 6.88 Got: " + t1Res;

        // Case 2: Per Quantity (non-tiered) with Max Commission as Trade Value Percentage
        CommissionQuery test2 = new CommissionQuery(" {\"Instr\": \"TSLA\", \"InstrType\": 1, \"Market\": \"NMS\", \"Currency\": \"USD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t2Res = ss.calculateCom(test2, 2, 0.005, "USD", 1.0, 0.01, 2, 0, 0.0, 0.0).origin;
        assert t2Res.equals(1.0) : "Expected: 1.0 Got: " + t2Res;

        // Case 3: Per Quantity (non-tiered) with Max Commission as Trade Value Percentage (Min > Max -> Max is adopted)
        CommissionQuery test3 = new CommissionQuery(" {\"Instr\": \"ACST\", \"InstrType\": 1, \"Market\": \"NCM\", \"Currency\": \"USD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t3Res = ss.calculateCom(test3, 2, 0.005, "USD", 0.0, 0.01, 2, 0, 0.0, 0.0).origin;
        assert t3Res.equals(0.2) : "Expected: 0.2 Got: " + t3Res;

        // Case 4: Per Quantity (non-tiered) with Max Commission as Fixed Rate
        CommissionQuery test4 = new CommissionQuery(" {\"Instr\": \"TSLA\", \"InstrType\": 1, \"Market\": \"NMS\", \"Currency\": \"USD\", \"Quantity\": 1000, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t4Res = ss.calculateCom(test4, 2, 0.01, "USD", 4.95, 9.95, 1, 0, 0.0, 0.0).origin;
        assert t4Res.equals(9.95) : "Expected: 9.95 Got: " + t4Res;

        // Case 5: Per Quantity (non-tiered) without Max Commission
        CommissionQuery test5 = new CommissionQuery(" {\"Instr\": \"TSLA\", \"InstrType\": 1, \"Market\": \"NMS\", \"Currency\": \"USD\", \"Quantity\": 10000, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t5Res = ss.calculateCom(test5, 2, 0.005, "USD", 1.0, 0.0, 0, 0, 0.0, 0.0).origin;
        assert t5Res.equals(50.0) : "Expected: 10.0 Got: " + t5Res;

        // Case 6: Per Quantity (tiered by quantity) within Lower Tier
        CommissionQuery test6 = new CommissionQuery(" {\"Instr\": \"TSLA\", \"InstrType\": 1, \"Market\": \"NMS\", \"Currency\": \"USD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t6Res = ss.calculateCom(test6, 3, 0.005, "USD", 1.0, 0.0, 0, 0, 0.0, 0.0).origin;
        assert t6Res.equals(1.0) : "Expected: 1.0 Got: " + t6Res;

        // Case 7: Per Quantity (tiered by quantity) within Upper Tier
        CommissionQuery test7 = new CommissionQuery(" {\"Instr\": \"DOL.TO\", \"InstrType\": 1, \"Market\": \"TOR\", \"Currency\": \"CAD\", \"Quantity\": 3000, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t7Res = ss.calculateCom(test7, 3, 0.01, "CAD", 19.96, 0.0, 0, 2001, 0.0, 19.5).origin;
        assert t7Res.equals(29.5) : "Expected: 29.5 Got: " + t7Res;

        // Case 8: Per Trade Value (non-tiered)
        CommissionQuery test8 = new CommissionQuery(" {\"Instr\": \"7974.T\", \"InstrType\": 1, \"Market\": \"JPX\", \"Currency\": \"JPY\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t8Res = ss.calculateCom(test8, 4, 0.0115, "JPY", 57500.0, 0.0, 0, 0, 5000000.0, 0.0).origin;
        Double t8ResExpected = 0.0115 * test8.getInstrPrice() * 100;
        assert t8Res.equals(t8ResExpected) : "Expected: " + t8ResExpected + " Got: " + t8Res;

        // Case 9: Per Trade Value (tiered by trade value) within Lower Tier
        CommissionQuery test9 = new CommissionQuery(" {\"Instr\": \"TATASTEEL.BO\", \"InstrType\": 1, \"Market\": \"BSE\", \"Currency\": \"INR\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t9Res = ss.calculateCom(test9, 5, 0.0001, "INR", 6.0, 20.0, 1, 0, 0.0, 0.0).origin;
        Double t9ResExpected = 6.0;
        assert t9Res.equals(t9ResExpected)  : "Expected: " + t9ResExpected + " Got: " + t9Res;

        // Case 10: Per Trade Value (tiered by trade value) within Upper Tier
        CommissionQuery test10 = new CommissionQuery(" {\"Instr\": \"TATASTEEL.BO\", \"InstrType\": 1, \"Market\": \"BSE\", \"Currency\": \"INR\", \"Quantity\": 3000, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
        Double t10Res = ss.calculateCom(test10, 5, 0.0002, "INR", 6.0, 0.0, 0, 0, 1000000.0, 20.0).origin;
        Double t10ResExpected = 0.0002 * (test10.getInstrPrice() * 3000 - 1000000) + 20;
        assert t10Res.equals(t10ResExpected) : "Expected: " + t10ResExpected + " Got: " + t10Res;

    }
}
