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
        String result = this.iss.searchInstrument("DOL.TO");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Dollarama Inc.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchUS() throws Exception {
        String result = this.iss.searchInstrument("MSFT");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Microsoft Corporation") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchMEX() throws Exception {
        String result = this.iss.searchInstrument("AGUA.MX");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Grupo Rotoplas S.A.B. de C.V.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchVIE() throws Exception {
        String result = this.iss.searchInstrument("VOE.VI");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Voestalpine AG") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchTAL() throws Exception {
        String result = this.iss.searchInstrument("ARC1T.TL");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Arco Vara AS") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchRIS() throws Exception {
        String result = this.iss.searchInstrument("GRD1R.RG");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Akciju sabiedriba Grindeks") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchLIT() throws Exception {
        String result = this.iss.searchInstrument("APG1L.VS");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("APB Apranga") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchBRU() throws Exception {
        String result = this.iss.searchInstrument("ACCB.BR");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Accentis N.V.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchPAR() throws Exception {
        String result = this.iss.searchInstrument("2CRSI.PA");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("2CRSI S.A.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchBUD() throws Exception {
        String result = this.iss.searchInstrument("AKKO.BD");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("AKKO Invest Nyrt.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchTLV() throws Exception {
        String result = this.iss.searchInstrument("ADKA.TA");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Adika Style Ltd.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchMIL() throws Exception {
        String result = this.iss.searchInstrument("SES.MI");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("SeSa S.p.A.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchAMS() throws Exception {
        String result = this.iss.searchInstrument("AMG.AS");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("AMG Advanced Metallurgical Group N.V.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchOSL() throws Exception {
        String result = this.iss.searchInstrument("OTS.OL");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Oceanteam ASA") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchLIS() throws Exception {
        String result = this.iss.searchInstrument("CDU.LS");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Conduril - Engenharia, S.A.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchMCE() throws Exception {
        String result = this.iss.searchInstrument("AENA.MC");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Aena S.M.E., S.A.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchSTO() throws Exception {
        String result = this.iss.searchInstrument("AAK.ST");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("AAK AB (publ.)") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchEBS() throws Exception {
        String result = this.iss.searchInstrument("ASCN.SW");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Ascom Holding AG") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchLSE() throws Exception {
        String result = this.iss.searchInstrument("OPM.L");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("1pm plc") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchIOB() throws Exception {
        String result = this.iss.searchInstrument("ACID.IL");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("shortname").getAsString().equals("ACER INCORPORATED GDR (REPR 5 S") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchATH() throws Exception {
        String result = this.iss.searchInstrument("EXAE.AT");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Hellenic Exchanges - Athens Stock Exchange SA") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchHEL() throws Exception {
        String result = this.iss.searchInstrument("CTY1S.HE");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Citycon Oyj") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchFRA() throws Exception {
        String result = this.iss.searchInstrument("AIR.F");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Airbus SE") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchSTU() throws Exception {
        String result = this.iss.searchInstrument("STG.SG");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("shortname").getAsString().equals("STINAG Stuttgart Invest AG Inha") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchGER() throws Exception {
        String result = this.iss.searchInstrument("AIR.DE");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Airbus SE") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchCPH() throws Exception {
        String result = this.iss.searchInstrument("CBRAIN.CO");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("cBrain A/S") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchISE() throws Exception {
        String result = this.iss.searchInstrument("APGN.IR");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Applegreen plc") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchJNB() throws Exception {
        String result = this.iss.searchInstrument("ACS.JO");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Acsion Limited") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchASX() throws Exception {
        String result = this.iss.searchInstrument("ANZ.AX");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Australia and New Zealand Banking Group Limited") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchHKG() throws Exception {
        String result = this.iss.searchInstrument("0005.HK");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("HSBC Holdings plc") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchSHH() throws Exception {
        String result = this.iss.searchInstrument("600519.SS");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Kweichow Moutai Co., Ltd.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchSHZ() throws Exception {
        String result = this.iss.searchInstrument("000839.SZ");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("CITIC Guoan Information Industry Co., Ltd.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchBSE() throws Exception {
        String result = this.iss.searchInstrument("GPPL.BO");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Gujarat Pipavav Port Limited") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchJPX() throws Exception {
        String result = this.iss.searchInstrument("9861.T");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Yoshinoya Holdings Co., Ltd.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchFKA() throws Exception {
        String result = this.iss.searchInstrument("2503.F");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Kirin Holdings CompanyLimited") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchSAP() throws Exception {
        String result = this.iss.searchInstrument("3055.S");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Hokuyaku Takeyama Holdings,Inc.") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchSES() throws Exception {
        String result = this.iss.searchInstrument("C6L.SI");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Singapore Airlines Limited") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchJKT() throws Exception {
        String result = this.iss.searchInstrument("BBCA.JK");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("PT Bank Central Asia Tbk") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchKLS() throws Exception {
        String result = this.iss.searchInstrument("5182.KL");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("shortname").getAsString().equals("MCT") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchSET() throws Exception {
        String result = this.iss.searchInstrument("TASCO.BK");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Tipco Asphalt Public Company Limited") :
                "Could not find instrument";
    }

    @Test
    void testInstrumentSearchNZE() throws Exception {
        String result = this.iss.searchInstrument("ANZ.NZ");
        JsonElement je = JsonParser.parseString(result);
        assert je.getAsJsonObject().getAsJsonArray("quotes").get(0)
                .getAsJsonObject().getAsJsonPrimitive("longname").getAsString().equals("Australia and New Zealand Banking Group Limited") :
                "Could not find instrument";
    }
}
