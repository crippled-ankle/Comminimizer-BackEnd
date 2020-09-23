package com.comminimizer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties
public class ConfigProperties {

    public static String FX_RATE_QUERY_URL_PREFIX;
    public static String FX_RATE_QUERY_URL_SUFFIX;
    public static String INSTRUMENT_QUOTE_URL_PREFIX;
    public static String INSTRUMENT_SEARCH_URL_PREFIX;
    public static String HTTP_HEADER_API_HOST_NAME;
    public static String HTTP_HEADER_API_KEY_NAME;
    public static String HTTP_HEADER_API_HOST;
    public static String HTTP_HEADER_API_KEY;
    public static String DB_JDBC_CONNECTION_LINK;
    public static String DB_USERNAME;
    public static String DB_PASSWORD;

    public String getFX_RATE_QUERY_URL_PREFIX(){
        return FX_RATE_QUERY_URL_PREFIX;
    }

    public String getFX_RATE_QUERY_URL_SUFFIX(){
        return FX_RATE_QUERY_URL_SUFFIX;
    }

    public String getINSTRUMENT_QUOTE_URL_PREFIX() {
        return INSTRUMENT_QUOTE_URL_PREFIX;
    }

    public String getINSTRUMENT_SEARCH_URL_PREFIX() {
        return INSTRUMENT_SEARCH_URL_PREFIX;
    }

    public String getHTTP_HEADER_API_HOST_NAME() {
        return HTTP_HEADER_API_HOST_NAME;
    }

    public String getHTTP_HEADER_API_KEY_NAME() {
        return HTTP_HEADER_API_KEY_NAME;
    }

    public String getHTTP_HEADER_API_HOST() {
        return HTTP_HEADER_API_HOST;
    }

    public String getHTTP_HEADER_API_KEY() {
        return HTTP_HEADER_API_KEY;
    }

    public String getDB_JDBC_CONNECTION_LINK() {
        return DB_JDBC_CONNECTION_LINK;
    }

    public String getDB_USERNAME() {
        return DB_USERNAME;
    }

    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    public void setFX_RATE_QUERY_URL_PREFIX(String s) {
        FX_RATE_QUERY_URL_PREFIX = s;
    }

    public void setFX_RATE_QUERY_URL_SUFFIX(String s) {
        FX_RATE_QUERY_URL_SUFFIX = s;
    }

    public void setINSTRUMENT_QUOTE_URL_PREFIX(String s) {
        INSTRUMENT_QUOTE_URL_PREFIX = s;
    }

    public void setINSTRUMENT_SEARCH_URL_PREFIX(String s) {
        INSTRUMENT_SEARCH_URL_PREFIX = s;
    }

    public void setHTTP_HEADER_API_HOST_NAME(String s) {
        HTTP_HEADER_API_HOST_NAME = s;
    }

    public void setHTTP_HEADER_API_KEY_NAME(String s) {
        HTTP_HEADER_API_KEY_NAME = s;
    }

    public void setHTTP_HEADER_API_HOST(String s) {
        HTTP_HEADER_API_HOST = s;
    }

    public void setHTTP_HEADER_API_KEY(String s) {
        HTTP_HEADER_API_KEY = s;
    }

    public void setDB_JDBC_CONNECTION_LINK(String s) {
        DB_JDBC_CONNECTION_LINK = s;
    }

    public void setDB_USERNAME(String s) {
        DB_USERNAME = s;
    }

    public void setDB_PASSWORD(String s) {
        DB_PASSWORD = s;
    }
}