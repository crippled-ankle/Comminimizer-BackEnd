package com.comminimizer.Query;

import com.comminimizer.Instrument.Instrument;
import com.google.gson.JsonElement;
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
        JsonParser jp = new JsonParser();
        rb = rb.replace("\\", "");
        JsonElement je = jp.parse(rb.substring(1, rb.length() - 1)); //TODO: justify the reason to truncate
        JsonObject jo = je.getAsJsonObject();
        Currency = jo.get("Currency").toString();
        Instr = jo.get("Instr").toString();
        InstrType = Integer.parseInt(jo.get("InstrType").toString());
        Region = jo.get("Market").toString();
        Quantity = Double.parseDouble(jo.get("Quantity").toString());
        AccountType = Integer.parseInt(jo.get("AccountType").toString());
        Instrument i = new Instrument(Instr, 1); // TODO set proper iden type
        i.setAttrFromSearch();
        InstrPrice = i.referencePrice;
        setTradeValue();
        setMarketQuery();
        setAccountTypeQuery();
    }

    //AccountType: 1 - Regular Only, 2 - Tax Shelter Only, 3 - All
    void setAccountTypeQuery(){
        List<String> ret = new java.util.ArrayList<>(Collections.emptyList());
        if(this.AccountType == 1){
            ret.addAll(REGULAR_ACCOUNT);
        } else if(this.AccountType == 2){
            ret.addAll(REGISTERED_ACCOUNT);
        } else if(this.AccountType == 3){
            ret.addAll(REGULAR_ACCOUNT);
            ret.addAll(REGISTERED_ACCOUNT);
        }
        this.AccountTypeQuery = ret;
    }

    //TODO refine the calculation of trade value as commission currency can be different than instrument quoting currency
    void setTradeValue() {
        this.TradeValue = this.Quantity * this.InstrPrice;
    }

    //TODO Add region mapping (know the API first)
    void setMarketQuery(){
        if(this.Region.equals("\"Toronto\""))
            this.MarketQuery =  "Canada";
        else
            this.MarketQuery =  this.Region.replace("\"", "");
    }

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
