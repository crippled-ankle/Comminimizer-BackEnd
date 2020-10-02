package com.comminimizer.Services;

import com.comminimizer.Query.CommissionQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CommissionQueryTest {

    private final String q1 = " {\"Instr\": \"DOL.TO\", \"InstrType\": 1, \"Market\": \"TOR\", \"Currency\": \"CAD\", \"Quantity\": 1, \"QuantityType\": \"shares\", \"AccountType\": 3} ";
    private final String q2 = " {\"Instr\": \"MSFT\", \"InstrType\": 1, \"Market\": \"NMS\", \"Currency\": \"USD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 1} ";
    private final String q3 = " {\"Instr\": \"DOL.TO\", \"InstrType\": 1, \"Market\": \"TOR\", \"Currency\": \"CAD\", \"Quantity\": 1000, \"QuantityType\": \"shares\", \"AccountType\": 2} ";
    private final String q4 = " {\"Instr\": \"CME.CN\", \"InstrType\": 1, \"Market\": \"CNQ\", \"Currency\": \"CAD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 1} ";
    private final String q5 = " {\"Instr\": \"RIWI.V\", \"InstrType\": 1, \"Market\": \"VAN\", \"Currency\": \"CAD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 1} ";
    private final String q6 = " {\"Instr\": \"HALO.NE\", \"InstrType\": 1, \"Market\": \"NEO\", \"Currency\": \"CAD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 1} ";
    private final String q7 = " {\"Instr\": \"AA\", \"InstrType\": 1, \"Market\": \"NYQ\", \"Currency\": \"USD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 1} ";
    private final String q8 = " {\"Instr\": \"CTIC\", \"InstrType\": 1, \"Market\": \"NCM\", \"Currency\": \"USD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 1} ";
    private final String q9 = " {\"Instr\": \"JG\", \"InstrType\": 1, \"Market\": \"NGM\", \"Currency\": \"USD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 1} ";
    private final String q10 = " {\"Instr\": \"6862.HK\", \"InstrType\": 1, \"Market\": \"HKG\", \"Currency\": \"HKD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 1} ";

    @Test
    void testGetAccountTypeQuery() throws Exception {
        List<String> t1Res = new CommissionQuery(q1).getAccountTypeQuery();
        List<String> t2Res = new CommissionQuery(q2).getAccountTypeQuery();
        List<String> t3Res = new CommissionQuery(q3).getAccountTypeQuery();
        assert t1Res.equals(Arrays.asList("Regular", "RRSP", "TFSA")) : "Expected: 'Regular', 'RRSP', 'TFSA', Got: " + t1Res;
        assert t2Res.equals(Arrays.asList("Regular")) : "Expected: 'Regular', Got: " + t2Res;
        assert t3Res.equals(Arrays.asList("RRSP", "TFSA")) : "Expected: 'RRSP', 'TFSA', Got: " + t3Res;
    }

    @Test
    void testGetMarketQuery() throws Exception {
        String t1Res = new CommissionQuery(q1).getMarketQuery();
        String t2Res = new CommissionQuery(q2).getMarketQuery();
        String t4Res = new CommissionQuery(q4).getMarketQuery();
        String t5Res = new CommissionQuery(q5).getMarketQuery();
        String t6Res = new CommissionQuery(q6).getMarketQuery();
        String t7Res = new CommissionQuery(q7).getMarketQuery();
        String t8Res = new CommissionQuery(q8).getMarketQuery();
        String t9Res = new CommissionQuery(q9).getMarketQuery();
        String t10Res = new CommissionQuery(q10).getMarketQuery();
        assert t1Res.equals("CA") : "Expected: CA, Got " + t1Res;
        assert t2Res.equals("US") : "Expected: US, Got " + t2Res;
        assert t4Res.equals("CA") : "Expected: CA, Got " + t4Res;
        assert t5Res.equals("CA") : "Expected: CA, Got " + t5Res;
        assert t6Res.equals("CA") : "Expected: CA, Got " + t6Res;
        assert t7Res.equals("US") : "Expected: US, Got " + t7Res;
        assert t8Res.equals("US") : "Expected: US, Got " + t8Res;
        assert t9Res.equals("US") : "Expected: US, Got " + t9Res;
        assert t10Res.equals("HKG") : "Expected: HKG, Got " + t10Res;
    }

    @Test
    void testGetQuantity() throws Exception {
        Double t1Res = new CommissionQuery(q1).getQuantity();
        assert t1Res == 1.0 : "Expected: 1, Got: " + t1Res;
    }

    @Test
    void testGetInstrPrice() throws Exception {
        assert new CommissionQuery(q1).getInstrPrice() > 0.0 : "Wrong Instrument Price";
    }

    @Test
    void testGetTradeValue() throws Exception {
        Double t1Res = new CommissionQuery(q1).getTradeValue();
        Double t2Res = new CommissionQuery(q2).getTradeValue();
        Double t3Res = new CommissionQuery(q3).getTradeValue();
        assert t1Res > 0.0 : "Wrong Trade Value";
        assert t2Res > 0.0 : "Wrong Trade Value";
        assert t3Res > 0.0 : "Wrong Trade Value";
    }
}
