package com.comminimizer.Query;

import com.comminimizer.Instrument.Instrument;
import com.comminimizer.Services.ComMinimizer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommissionQuery{
    Integer AccountType;
    String Currency;
    String Instr;
    Double InstrPrice;
    Integer InstrType;
    String Region;
    Double Quantity;
    Double TradeValue;
    String MarketQuery;
    List<String> AccountTypeQuery;

    static final List<String> REGULAR_ACCOUNT = Arrays.asList("Regular");
    static final List<String> REGISTERED_ACCOUNT = Arrays.asList("RRSP", "TFSA");

    public CommissionQuery(String rb){
        rb = rb.replace("\\", "");
        try {
            JsonObject jo = JsonParser.parseString(rb.substring(1, rb.length() - 1)).getAsJsonObject(); //TODO: justify the reason to truncate
            Instr = jo.get("Instr").toString();
            InstrType = Integer.parseInt(jo.get("InstrType").toString());
            Region = jo.get("Market").toString();
            Quantity = Double.parseDouble(jo.get("Quantity").toString());
            AccountType = Integer.parseInt(jo.get("AccountType").toString());
            Instrument i = new Instrument(Instr, 1); // TODO set proper iden type
            i.setAttrFromSearch();
            InstrPrice = i.referencePrice;
            Currency = i.currency;
            setTradeValue();
            setMarketQuery();
            setAccountTypeQuery();
        } catch ( Exception ex ) {
            ex.printStackTrace();
            ComMinimizer.log.warn("Unable to parse commission search query: " + rb);
        }
    }

    //AccountType: 1 - Regular Only, 2 - Registered Only, 3 - All
    void setAccountTypeQuery(){
        List<String> ret = new java.util.ArrayList<>(Collections.emptyList());
        if(this.AccountType.equals(1)){
            ret.addAll(REGULAR_ACCOUNT);
        } else if(this.AccountType.equals(2)){
            ret.addAll(REGISTERED_ACCOUNT);
        } else if(this.AccountType.equals(3)){
            ret.addAll(REGULAR_ACCOUNT);
            ret.addAll(REGISTERED_ACCOUNT);
        }
        this.AccountTypeQuery = ret;
    }

    void setTradeValue() {
        this.TradeValue = this.Quantity * this.InstrPrice;
    }

    void setMarketQuery(){
        if(this.Region.equals("\"TOR\"")
            || this.Region.equals("\"VAN\"")
            || this.Region.equals("\"CNQ\"")
            || this.Region.equals("\"NEO\""))
            this.MarketQuery =  "CA";
        else if(this.Region.equals("\"NMS\"")
            || this.Region.equals("\"NGM\"")
            || this.Region.equals("\"NCM\"")
            || this.Region.equals("\"NYQ\""))
            this.MarketQuery = "US";
        else
            this.MarketQuery =  this.Region.replace("\"", "");
        ComMinimizer.log.info(this.MarketQuery);
    }

    public String getCurrency() { return Currency; }

    public Double getQuantity() {
        return Quantity;
    }

    public Double getInstrPrice() {
        return InstrPrice;
    }

    public Double getTradeValue() {
        return TradeValue;
    }

    public List<String> getAccountTypeQuery() {
        return AccountTypeQuery;
    }

    public String getMarketQuery() {
        return MarketQuery;
    }
}
