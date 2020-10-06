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

    /**
     * Constructor for a {@code CommissionQuery} object based on values supplied by the user.
     * It references instrument related information from Yahoo API
     * @param rb user supplied query in {@code Json} format
     */
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

    /**
     * It enriches the query with the desired account type
     * AccountType: 1 - Regular Only, 2 - Registered Only, 3 - All
     */
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

    /**
     * It sets the trade value of the query
     */
    void setTradeValue() {
        this.TradeValue = this.Quantity * this.InstrPrice;
    }

    /**
     * It maps the supplied region to the region used to query
     * commission database, and sets the attribute of the query object
     */
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

    /**
     * Getter for the quoting currency of the instrument
     * @return the currency code
     */
    public String getCurrency() { return Currency; }

    /**
     * Getter for the quantity of the search query
     * @return quantity from user input
     */
    public Double getQuantity() {
        return Quantity;
    }

    /**
     * Getter for the price of the instrument
     * @return price value
     */
    public Double getInstrPrice() {
        return InstrPrice;
    }

    /**
     * Getter for the trade value of the query
     * @return trade value
     */
    public Double getTradeValue() {
        return TradeValue;
    }

    /**
     * Getter for the account type attribute
     * @return a formatted string representing the account type
     *         used in the query
     */
    public List<String> getAccountTypeQuery() {
        return AccountTypeQuery;
    }

    /**
     * Getter for the market attribute
     * @return a mapped region value to be used in querying
     *         commission database
     */
    public String getMarketQuery() {
        return MarketQuery;
    }
}
