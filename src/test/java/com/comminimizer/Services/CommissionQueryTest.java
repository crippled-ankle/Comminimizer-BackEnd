package com.comminimizer.Services;

import com.comminimizer.Query.CommissionQuery;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CommissionQueryTest {
    private CommissionQuery q1 = new CommissionQuery(" {\"Instr\": \"DOL.TRT\", \"InstrType\": 1, \"Market\": \"Toronto\", \"Currency\": \"CAD\", \"Quantity\": 1, \"QuantityType\": \"shares\", \"AccountType\": 3} ");
    private CommissionQuery q2 = new CommissionQuery(" {\"Instr\": \"MSFT\", \"InstrType\": 1, \"Market\": \"United States\", \"Currency\": \"USD\", \"Quantity\": 100, \"QuantityType\": \"shares\", \"AccountType\": 1} ");
    private CommissionQuery q3 = new CommissionQuery(" {\"Instr\": \"DOL.TRT\", \"InstrType\": 1, \"Market\": \"Toronto\", \"Currency\": \"CAD\", \"Quantity\": 1000, \"QuantityType\": \"shares\", \"AccountType\": 2} ");

    @Test
    void testGetAccountTypeQuery() throws Exception {
        List<String> t1Res = q1.getAccountTypeQuery();
        List<String> t2Res = q2.getAccountTypeQuery();
        List<String> t3Res = q3.getAccountTypeQuery();
        assert t1Res.equals(Arrays.asList("Regular", "RRSP", "TFSA")) : "Expected: 'Regular', 'RRSP', 'TFSA', Got: " + t1Res;
        assert t2Res.equals(Arrays.asList("Regular")) : "Expected: 'Regular', Got: " + t2Res;
        assert t3Res.equals(Arrays.asList("RRSP", "TFSA")) : "Expected: 'RRSP', 'TFSA', Got: " + t3Res;
    }

    @Test
    void testGetMarketQuery() throws Exception {
        String t1Res = q1.getMarketQuery();
        String t2Res = q2.getMarketQuery();
        assert t1Res.equals("Canada") : "Expected: Canada, Got " + t1Res;
        assert t2Res.equals("United States") : "Expected: United States, Got " + t2Res;
    }

    @Test
    void testGetQuantity() throws Exception {
        Double t1Res = q1.getQuantity();
        assert t1Res == 1.0 : "Expected: 1, Got: " + t1Res;
    }

    @Test
    void testGetInstrPrice() throws Exception {
        assert q1.getInstrPrice() > 0.0 : "Wrong Instrument Price";
    }

    @Test
    void testGetTradeValue() throws Exception {
        Double t1Res = q1.getTradeValue();
        Double t2Res = q2.getTradeValue();
        Double t3Res = q3.getTradeValue();
        System.out.println(t1Res);
        assert t1Res > 0.0 : "Wrong Trade Value";
        assert t2Res > 0.0 : "Wrong Trade Value";
        assert t3Res > 0.0 : "Wrong Trade Value";
    }
}
