package com.comminimizer.Services;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InstrumentSearchServiceTest {

    @Autowired
    private InstrumentSearchService iss;

    @Test
    void testInstrumentSearchCanada() throws Exception {
        String result = this.iss.searchInstrument("DOL.TRT");
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(result);
        assert je.getAsJsonObject().getAsJsonArray("bestMatches").get(0)
                .getAsJsonObject().getAsJsonPrimitive("2. name").getAsString().equals("Dollarama Inc.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchUS() throws Exception {
        String result = this.iss.searchInstrument("MSFT");
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(result);
        assert je.getAsJsonObject().getAsJsonArray("bestMatches").get(0)
                .getAsJsonObject().getAsJsonPrimitive("2. name").getAsString().equals("Microsoft Corporation") :
                "Could not find instrument";
    }
}
