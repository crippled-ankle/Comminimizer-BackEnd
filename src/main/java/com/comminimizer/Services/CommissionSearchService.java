package com.comminimizer.Services;

import com.comminimizer.Query.CommissionQuery;
import com.comminimizer.Query.FXQuery;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.PriorityQueue;

@Service
public class CommissionSearchService {
    static final String UNIFIED_CURRENCY_CODE = "CAD";
    static final Integer RESULT_INITIAL_CAPACITY = 25;
    static final String DB_JDBC_CONNECTION_LINK = "ADD_HERE";
    static final String DB_USERNAME = "ADD_HERE";
    static final String DB_PASSWORD = "ADD_HERE";

    public class Quote {
       Double origin;
       String originCode;
       Double unified;
       String unifiedCode;
    }

    //TODO refine the calculation of trade value as commission currency can be different than instrument quoting currency
    public Double calculateTradeValue(Double quantity, Double instrPrice) {
        return quantity * instrPrice;
    }

    public Quote calculateCom(CommissionQuery s,
                              Integer comType,
                              Double comRate,
                              String curCode,
                              Double minCom,
                              Double maxCom,
                              Double tradeValue,
                              Integer maxComType,
                              Integer tierStart,
                              Double additionalCost) {
        Quote ret = new Quote();
        ret.originCode = curCode;
        ret.unifiedCode = UNIFIED_CURRENCY_CODE;
        if(comType == 1) { // flat rate
            ret.origin = comRate;
        } else if(comType == 2) { // pay per use
            ret.origin = comRate * s.getQuantity();
        } else if(comType == 3) { // pay per use (tiered)
            ret.origin = comRate * (s.getQuantity() - tierStart + 1);
        }

        // set quote cap
        if(maxComType == 2) { // trade value percentage
            maxCom = maxCom * tradeValue;
        }
        // IB #7, applicable to US only
        if(maxCom < minCom){ // if max < min, bring min down to max value so max is assessed
            minCom = maxCom;
        }
        ret.origin = Math.min(maxCom, ret.origin);
        ret.origin += additionalCost;
        ret.origin = Math.max(minCom, ret.origin);
        FXQuery q = new FXQuery(ret.originCode, ret.unifiedCode);
        ret.unified = ret.origin * q.getRate();
        return ret;
    }

    Integer compare(JsonObject o1, JsonObject o2){
        if(o1.get("ComAmountUnified").getAsDouble() > o2.get("ComAmountUnified").getAsDouble())
            return 1;
        else if(o1.get("ComAmountUnified").getAsDouble() < o2.get("ComAmountUnified").getAsDouble())
            return -1;
        else {
            return Integer.compare(o1.get("BrokerName").getAsString().compareTo(o2.get("BrokerName").getAsString()), 0);
        }
    }

    public String queryCommissionDB(String requestBody) {
        CommissionQuery s = new CommissionQuery(requestBody);
        String accountTypeQuery = s.getAccountTypeQuery();
        String marketQuery = s.getMarketQuery();
        Double instrPrice = s.getInstrPrice();
        Double quantity = s.getQuantity();
        Double tradeValue = calculateTradeValue(quantity, instrPrice);
        String sqlFindAllComEntries = "select b.Name, c.*, at.Description as Account_Type_Desc, cc.Code as Currency_Code\n" +
                                        "from commission c, market m, broker b, account_type at, currency cc\n" +
                                        "where c.Market = m.ID\n" +
                                        "and c.Broker_ID = b.ID\n" +
                                        "and at.ID = c.Account_Type\n" +
                                        "and cc.ID = c.Currency\n" +
                                        "and (c.Trade_Value_Range_Lower is null or (c.Trade_Value_Range_Lower <=" + tradeValue + "and c.Trade_Value_Range_Upper >=" + tradeValue + ") )\n" +
                                        "and (c.Instrument_Price_Lower is null or (c.Instrument_Price_Lower <=" + instrPrice + "and c.Instrument_Price_Upper >=" + instrPrice + ") )\n" +
                                        "and (c.Number_Unit_Lower is null or (c.Number_Unit_Lower <=" + quantity + "and c.Number_Unit_Upper >=" + quantity + ") )\n" +
                                        "and at.Description in (" + accountTypeQuery + ")\n" +
                                        "and m.Description = " + marketQuery + ";";
        String connectionUrl = DB_JDBC_CONNECTION_LINK;
        PriorityQueue<JsonObject> pq = new PriorityQueue<>(RESULT_INITIAL_CAPACITY, this::compare);
        JsonArray ja = new JsonArray();
        try (Connection conn = DriverManager.getConnection(connectionUrl, DB_USERNAME, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sqlFindAllComEntries);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                JsonObject jo = new JsonObject();
                jo.addProperty("BrokerID", rs.getInt("Broker_ID"));
                jo.addProperty("BrokerName", rs.getString("Name"));
                jo.addProperty("AccountType", rs.getString("Account_Type_Desc"));
                jo.addProperty("ComCurrencyOrigin", rs.getString("Currency_Code"));
                Quote q = calculateCom(s, rs.getInt("Commission_Type"),
                        rs.getDouble("Commission_Rate"),
                        rs.getString("Currency_Code"),
                        rs.getDouble("Min_Commission"),
                        rs.getDouble("Max_Commission"),
                        tradeValue,
                        rs.getInt("Max_Commission_Type"),
                        rs.getInt("Number_Unit_Lower"),
                        rs.getDouble("Additional_Cost"));
                jo.addProperty("ComAmountOrigin", q.origin);
                jo.addProperty("ComCurrencyUnified", q.unifiedCode);
                jo.addProperty("ComAmountUnified", q.unified);
                pq.add(jo);
            }
            while(pq.size() != 0){
                ja.add(pq.remove());
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return ja.toString();
    }
}
