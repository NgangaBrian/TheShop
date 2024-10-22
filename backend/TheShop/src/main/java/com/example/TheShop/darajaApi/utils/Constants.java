package com.example.TheShop.darajaApi.utils;

import okhttp3.MediaType;

import java.awt.*;

public class Constants {
    public static final String BASIC_AUTH_STRING = "Basic";
    public static final String BEARER_AUTH_STRING = "Bearer";
    public static final String CACHE_CONTROL_HEADER = "cache-control";
    public static final String CACHE_CONTROL_HEADER_VALUE = "no-cache";
    public static final String CUSTOMER_PAYBILL_ONLINE = "CustomerPayBillOnline";
    public static final String AUTHORIZATION_HEADER_STRING = "Authorization";
    public static MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
}
