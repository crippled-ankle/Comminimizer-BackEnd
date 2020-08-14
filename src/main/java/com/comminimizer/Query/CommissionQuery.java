package com.comminimizer.Query;

import com.comminimizer.Instrument.Instrument;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CommissionQuery{
    Integer AccountType;
    String Currency;
    String Instr;
    Double InstrPrice;
    Integer InstrType;
    String Region;
    Double Quantity;

    static final String REGULAR_ACCOUNT = "'Regular'";
    static final String REGISTERED_ACCOUNT = "'RRSP', 'TFSA'";

    public CommissionQuery(String rb){
        JsonParser jp = new JsonParser();
        rb = rb.replace("\\", "");
        JsonElement je = jp.parse(rb.substring(1, rb.length() - 1));
        JsonObject jo = je.getAsJsonObject();
        System.out.println(jo.toString());
        Currency = jo.get("Currency").toString();
        Instr = jo.get("Instr").toString();
        InstrType = Integer.parseInt(jo.get("InstrType").toString());
        Region = jo.get("Market").toString();
        Quantity = Double.parseDouble(jo.get("Quantity").toString());
        AccountType = Integer.parseInt(jo.get("AccountType").toString());
        Instrument i = new Instrument(Instr, 1); // TODO set proper iden type
        i.setAttrFromSearch();
        InstrPrice = i.referencePrice;
    }

    //AccountType: 1 - Regular Only, 2 - Tax Shelter Only, 3 - All
    public String getAccountTypeQuery(){
        String ret = "";
        if(this.AccountType == 1){
            ret += REGULAR_ACCOUNT;
        } else if(this.AccountType == 2){
            ret += REGISTERED_ACCOUNT;
        } else if(this.AccountType == 3){
            ret += REGULAR_ACCOUNT + ", " + REGISTERED_ACCOUNT;
        }
        return ret;
    }

    //TODO Add region mapping (know the API first)
    public String getRegionMapping(String r){
        if(r.equals("\"Toronto\""))
            return "Canada";
        else
            return r.replace("\"", "");
    }

    public String getMarketQuery(){
        return "'" + getRegionMapping(this.Region) + "'";
    }

    public Double getQuantity() {
        return Quantity;
    }

    public Double getInstrPrice() {
        return InstrPrice;
    }
}
