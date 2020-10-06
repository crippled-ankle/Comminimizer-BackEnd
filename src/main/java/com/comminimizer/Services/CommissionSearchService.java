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

import static com.comminimizer.ConfigProperties.*;

@Service
public class CommissionSearchService {

    @Autowired
    FXRateService fxs;

    static final String UNIFIED_CURRENCY_CODE = "CAD";
    static final Integer RESULT_INITIAL_CAPACITY = 25;
    static final Double NULL_DOUBLE = 0.0;

    /**
     * It uses JDBC connection URL, username, and password to establish a
     * connection to the database where all records collected from brokers
     * are stored.
     * @return the {@code DataSource} object corresponding to the commission
     *         database
     */
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.username(DB_USERNAME);
        dataSourceBuilder.password(DB_PASSWORD);
        dataSourceBuilder.url(DB_JDBC_CONNECTION_LINK);
        return dataSourceBuilder.build();
    }

    static class Quote {
        Double origin;
        String originCode;
        Double unified;
        String unifiedCode;
    }

    /**
     * Check if the fetched record is applicable to the {@code CommissionQuery},
     * e.g. if the trade value and the instrument price are within the range.
     * This function was isolated from the SQL query as trade value and price constraints
     * can be specified in different currencies by different brokers, and it is not feasible
     * to equip the database with all FX rates.
     * @param s the commission search query sent by the user
     * @param rCurrency the currency in which the check is done (and the commission is charged)
     * @param rInstrumentPriceLower the lower bound of the instrument price
     * @param rInstrumentPriceUpper the upper bound of the instrument price
     * @param rTradeValueRangeLower the lower bound of the trade value
     * @param rTradeValueRangeUpper the upper bound of the trade value
     * @return true if values of query satisfy all the constraints;
     *         false otherwise
     */
    public Boolean checkRecordConstraint(CommissionQuery s,
                                         String rCurrency,
                                         Double rInstrumentPriceLower,
                                         Double rInstrumentPriceUpper,
                                         Double rTradeValueRangeLower,
                                         Double rTradeValueRangeUpper) {
        Double tradeValue = s.getTradeValue();
        Double price = s.getInstrPrice();
        String qCurrency = s.getCurrency();
        // unify the currency
        if(!qCurrency.equals(rCurrency)){
            FXQuery q = new FXQuery(qCurrency, rCurrency);
            tradeValue = tradeValue * fxs.getRate(q);
        }
        if(!((rInstrumentPriceLower.equals(NULL_DOUBLE) && rInstrumentPriceUpper.equals(NULL_DOUBLE)) ||
                (price >= rInstrumentPriceLower && rInstrumentPriceUpper.equals(NULL_DOUBLE)) ||
                (price >= rInstrumentPriceLower && price < rInstrumentPriceUpper))) {
            return false;
        }
        if(!((rTradeValueRangeLower.equals(NULL_DOUBLE) && rTradeValueRangeUpper.equals(NULL_DOUBLE)) ||
                (tradeValue >= rTradeValueRangeLower && rTradeValueRangeUpper.equals(NULL_DOUBLE)) ||
                (tradeValue >= rTradeValueRangeLower && tradeValue < rTradeValueRangeUpper))) {
            return false;
        }
        return true;
    }

    /**
     * It calculates the commission amount based on the {@code CommissionQuery}
     * and values from a broker
     * @param s the commission search query sent by the user
     * @param comType the way in which commission is calculated
     * @param comRate the rate by which commission is charged
     * @param curCode the currency in which commission is charged
     * @param minCom the minimum commission charge
     * @param maxCom the maximum commission charge
     * @param maxComType the way in which max commission is calculated
     * @param tierStartQ the quantity the current tier starts from (if applicable)
     * @param tierStartTV the trade value the current tier starts from (if applicable)
     * @param additionalCost the amount in addition to the current tier (if applicable)
     * @return a {@code Quote} object containing commission in original currency
     *         and unified currency
     */
    public Quote calculateCom(CommissionQuery s,
                              Integer comType,
                              Double comRate,
                              String curCode,
                              Double minCom,
                              Double maxCom,
                              Integer maxComType,
                              Integer tierStartQ,
                              Double tierStartTV,
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
            ret.origin = comRate * (s.getQuantity() - tierStartQ + 1);
        } else if(comType == 4) { // per trade value
            ret.origin = comRate * tradeValue;
        } else if(comType == 5) { // per trade value (tiered)
            ret.origin = comRate * (s.getTradeValue() - tierStartTV);
        }
        // set quote cap
        if(maxComType == 2) { // trade value percentage
            maxCom = maxCom * tradeValue;
        }

        // cap the commission if there is a max
        if(maxComType != 0) {
            ret.origin = Math.min(maxCom, ret.origin);
        }
        ret.origin += additionalCost;

        ret.origin = Math.max(minCom, ret.origin);
        FXQuery q = new FXQuery(ret.originCode, ret.unifiedCode);
        ret.unified = ret.origin * fxs.getRate(q);
        return ret;
    }

    /**
     * The customized compare function to sort commission charges by amount and broker name
     * in the {@code PriorityQueue}.
     * @param o1 the first element to be compared
     * @param o2 the second element to be compared
     * @return -1 if the first should proceed the second in the sorted result
     *         1 if the second should proceed the first in the sorted result
     *         0 otherwise
     */
    Integer compare(JsonObject o1, JsonObject o2){
        if(o1.get("ComAmountUnified").getAsDouble() > o2.get("ComAmountUnified").getAsDouble())
            return 1;
        else if(o1.get("ComAmountUnified").getAsDouble() < o2.get("ComAmountUnified").getAsDouble())
            return -1;
        else {
            return Integer.compare(o1.get("BrokerName").getAsString().compareTo(o2.get("BrokerName").getAsString()), 0);
        }
    }

    /**
     * It queries the commission database based on the input query, filters out the
     * non-applicable records, calculates the amount charged by brokers, and sorts
     * the result.
     * @param requestBody the commission query sent by the user
     * @return the sorted result of the query in {@code Json} format
     */
    public String queryCommissionDB(String requestBody) {
        ComMinimizer.log.info("Received Commission Query: " + requestBody);
        CommissionQuery s = new CommissionQuery(requestBody);
        String sqlFindAllComEntries = "select b.Name, c.*, at.Description as Account_Type_Desc, cc.Code as Currency_Code\n" +
                                        "from commission c, market m, broker b, account_type at, currency cc\n" +
                                        "where c.Market = m.ID\n" +
                                        "and c.Broker_ID = b.ID\n" +
                                        "and at.ID = c.Account_Type\n" +
                                        "and cc.ID = c.Currency\n" +
                                        "and (c.Number_Unit_Lower is null or (c.Number_Unit_Lower <=:Quantity and (c.Number_Unit_Upper >=:Quantity or c.Number_Unit_Upper is NULL)) )\n" +
                                        "and at.Description in (:AccountTypeQuery)\n" +
                                        "and m.Code =:MarketQuery;";
        PriorityQueue<JsonObject> pq = new PriorityQueue<>(RESULT_INITIAL_CAPACITY, this::compare);
        NamedParameterJdbcTemplate jdbcTemplateObject = new NamedParameterJdbcTemplate(getDataSource());
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(s);
        JsonArray ja = jdbcTemplateObject.query(sqlFindAllComEntries, namedParameters,
                rs -> {
                    JsonArray ja1 = new JsonArray();
                    while (rs.next()) {
                        String rCurrency = rs.getString("Currency_Code");
                        Double rInstrumentPriceLower = rs.getDouble("Instrument_Price_Lower");
                        Double rInstrumentPriceUpper = rs.getDouble("Instrument_Price_Upper");
                        Double rTradeValueRangeLower = rs.getDouble("Trade_Value_Range_Lower");
                        Double rTradeValueRangeUpper = rs.getDouble("Trade_Value_Range_Upper");
                        if(checkRecordConstraint(s,
                                                rCurrency,
                                                rInstrumentPriceLower,
                                                rInstrumentPriceUpper,
                                                rTradeValueRangeLower,
                                                rTradeValueRangeUpper)){ // record fits constraints
                            JsonObject jo = new JsonObject();
                            jo.addProperty("BrokerID", rs.getInt("Broker_ID"));
                            jo.addProperty("BrokerName", rs.getString("Name"));
                            jo.addProperty("AccountType", rs.getString("Account_Type_Desc"));
                            jo.addProperty("ComCurrencyOrigin", rs.getString("Currency_Code"));
                            Quote q = calculateCom(s, rs.getInt("Commission_Type"),
                                                        rs.getDouble("Commission_Rate"),
                                                        rCurrency,
                                                        rs.getDouble("Min_Commission"),
                                                        rs.getDouble("Max_Commission"),
                                                        rs.getInt("Max_Commission_Type"),
                                                        rs.getInt("Number_Unit_Lower"),
                                                        rs.getDouble("Trade_Value_Range_Lower"),
                                                        rs.getDouble("Additional_Cost"));
                            jo.addProperty("ComAmountOrigin", q.origin);
                            jo.addProperty("ComCurrencyUnified", q.unifiedCode);
                            jo.addProperty("ComAmountUnified", q.unified);
                            pq.add(jo);
                        }
                    }
                    while(pq.size() != 0){
                        ja1.add(pq.remove());
                    }
                    return ja1;
                });
        if(ja == null){
            ComMinimizer.log.warn("Commission Query Resulted in Result Set Being Null: " + requestBody);
            return "[]";
        } else {
            ComMinimizer.log.info("Commission Query Result: " + ja.toString());
            return ja.toString();
        }
    }
}
