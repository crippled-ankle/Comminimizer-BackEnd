package com.comminimizer.Services;

import com.comminimizer.Query.CommissionQuery;
import com.comminimizer.Query.FXQuery;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.util.PriorityQueue;

@Service
public class CommissionSearchService {

    @Autowired
    FXRateService fxs;

    static final String UNIFIED_CURRENCY_CODE = "CAD";
    static final Integer RESULT_INITIAL_CAPACITY = 25;
    static final String DB_JDBC_CONNECTION_LINK = "ADD_HERE";
    static final String DB_USERNAME = "ADD_HERE";
    static final String DB_PASSWORD = "ADD_HERE";

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.username(DB_USERNAME);
        dataSourceBuilder.password(DB_PASSWORD);
        dataSourceBuilder.url(DB_JDBC_CONNECTION_LINK);
        return dataSourceBuilder.build();
    }

    public class Quote {
        Double origin;
        String originCode;
        Double unified;
        String unifiedCode;
    }

    public Quote calculateCom(CommissionQuery s,
                              Integer comType,
                              Double comRate,
                              String curCode,
                              Double minCom,
                              Double maxCom,
                              Integer maxComType,
                              Integer tierStart,
                              Double additionalCost) {
        Quote ret = new Quote();
        Double tradeValue = s.getTradeValue();
        String tradeValueCur = s.getCurrency();
        ret.originCode = curCode;
        ret.unifiedCode = UNIFIED_CURRENCY_CODE;
        if(!curCode.equals(tradeValueCur)){
            FXQuery tradeValueFXQuery = new FXQuery(tradeValueCur, curCode);
            tradeValue = tradeValue * fxs.getRate(tradeValueFXQuery);
        }
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

        ret.origin = Math.min(maxCom, ret.origin);
        ret.origin += additionalCost;
        ret.origin = Math.max(minCom, ret.origin);
        FXQuery q = new FXQuery(ret.originCode, ret.unifiedCode);
        ret.unified = ret.origin * fxs.getRate(q);
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
        String sqlFindAllComEntries = "select b.Name, c.*, at.Description as Account_Type_Desc, cc.Code as Currency_Code\n" +
                                        "from commission c, market m, broker b, account_type at, currency cc\n" +
                                        "where c.Market = m.ID\n" +
                                        "and c.Broker_ID = b.ID\n" +
                                        "and at.ID = c.Account_Type\n" +
                                        "and cc.ID = c.Currency\n" +
                                        "and (c.Trade_Value_Range_Lower is null or (c.Trade_Value_Range_Lower <=:TradeValue and (c.Trade_Value_Range_Upper >=:TradeValue  or c.Trade_Value_Range_Upper is NULL)) )\n" +
                                        "and (c.Instrument_Price_Lower is null or (c.Instrument_Price_Lower <=:InstrPrice and (c.Instrument_Price_Upper >=:InstrPrice or c.Instrument_Price_Upper is NULL)) )\n" +
                                        "and (c.Number_Unit_Lower is null or (c.Number_Unit_Lower <=:Quantity and (c.Number_Unit_Upper >=:Quantity or c.Number_Unit_Upper is NULL)) )\n" +
                                        "and at.Description in (:AccountTypeQuery)\n" +
                                        "and m.Description =:MarketQuery;";
        PriorityQueue<JsonObject> pq = new PriorityQueue<>(RESULT_INITIAL_CAPACITY, this::compare);
        NamedParameterJdbcTemplate jdbcTemplateObject = new NamedParameterJdbcTemplate(getDataSource());
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(s);
        JsonArray ja = jdbcTemplateObject.query(sqlFindAllComEntries, namedParameters,
                rs -> {
                    JsonArray ja1 = new JsonArray();
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
                                rs.getInt("Max_Commission_Type"),
                                rs.getInt("Number_Unit_Lower"),
                                rs.getDouble("Additional_Cost"));
                        jo.addProperty("ComAmountOrigin", q.origin);
                        jo.addProperty("ComCurrencyUnified", q.unifiedCode);
                        jo.addProperty("ComAmountUnified", q.unified);
                        pq.add(jo);
                    }
                    while(pq.size() != 0){
                        ja1.add(pq.remove());
                    }
                    return ja1;
                });
        return ja.toString();
    }
}
