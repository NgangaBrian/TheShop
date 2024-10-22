package com.example.TheShop.darajaApi.services;

import com.example.TheShop.darajaApi.config.MpesaConfiguration;
import com.example.TheShop.darajaApi.dtos.*;
import com.example.TheShop.darajaApi.utils.Constants;
import com.example.TheShop.darajaApi.utils.HelperUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

import static com.example.TheShop.darajaApi.utils.Constants.*;

@Service
@Slf4j
public class DarajaApiImpl implements DarajaApi {

    private final MpesaConfiguration mpesaConfiguration;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public DarajaApiImpl(MpesaConfiguration mpesaConfiguration, OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        this.mpesaConfiguration = mpesaConfiguration;
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public AccessTokenResponse getAccessToken() {

        // get Base64 rep of consumerkey + ":" + consumerSecret

        String encodedCredentials = HelperUtility.toBase64String(String.format("%s:%s", mpesaConfiguration.getConsumerKey(),
                mpesaConfiguration.getConsumerSecret()));

        Request request = new Request.Builder()
                .url(String.format("%s?grant_type=%s", mpesaConfiguration.getOauthEndpoint(), mpesaConfiguration.getGrantType()))
                .get()
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BASIC_AUTH_STRING, encodedCredentials))
                .addHeader(CACHE_CONTROL_HEADER, CACHE_CONTROL_HEADER_VALUE)
                .build();

        try {
            Response responsee = okHttpClient.newCall(request).execute();
            assert responsee.body() != null;
            log.info(String.valueOf(responsee.body()));

            // use jackson to decode
            return objectMapper.readValue(responsee.body().string(), AccessTokenResponse.class);

        } catch (IOException e) {
            log.error(String.format("Could not get access token: %s", e.getLocalizedMessage()));

            e.printStackTrace();
            return null;
        }

    }

    @Override
    public RegisterUrlResponse registerUrl() {
        AccessTokenResponse accessTokenResponse = getAccessToken();

        log.info("AccessTokenResponse: {}", accessTokenResponse);

        RegisterUrlRequest registerUrlRequest = new RegisterUrlRequest();

        registerUrlRequest.setConfirmationURL(mpesaConfiguration.getConfirmationUrl());
        registerUrlRequest.setResponseType(mpesaConfiguration.getResponseType());
        registerUrlRequest.setShortCode(mpesaConfiguration.getShortCode());
        registerUrlRequest.setValidationURL(mpesaConfiguration.getValidationUrl());

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, Objects.requireNonNull(HelperUtility.toJson(registerUrlRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getRegisterUrlEndpoint())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();


        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            String responseBody = response.body().string();
            log.info(responseBody);

            // use jackson to get response
            return objectMapper.readValue(responseBody, RegisterUrlResponse.class);
        } catch (IOException e) {
            log.info(String.valueOf(response.body()));
            log.error(String.format("Could not register url: %s", e.getLocalizedMessage()));
            throw new RuntimeException(e);
        }


    }

    @Override
    public STKPushSynchronousResponse performSTKPushRequest(InternalSTKPushRequest internalSTKPushRequest) {

        ExternalSTKPushRequest externalSTKPushRequest = new ExternalSTKPushRequest();
        externalSTKPushRequest.setBusinessShortCode(mpesaConfiguration.getShortCode());

        String transactionTimestamp = HelperUtility.getTransactionTimestamp();
        String stkPushPass = HelperUtility.getSTKPushPassword(mpesaConfiguration.getStkPushShortCode(),
                mpesaConfiguration.getStkPassKey(), transactionTimestamp);
        externalSTKPushRequest.setPassword(stkPushPass);
        externalSTKPushRequest.setTimestamp(transactionTimestamp);
        externalSTKPushRequest.setTransactionType(CUSTOMER_PAYBILL_ONLINE);
        externalSTKPushRequest.setAmount(internalSTKPushRequest.getAmount());
        externalSTKPushRequest.setPartyA(internalSTKPushRequest.getPhoneNumber());
        externalSTKPushRequest.setPartyB(mpesaConfiguration.getStkPushShortCode());
        externalSTKPushRequest.setPhoneNumber(internalSTKPushRequest.getPhoneNumber());
        externalSTKPushRequest.setCallBackURL("https://0b97-105-231-222-157.ngrok-free.app/mobile-money/stk-transaction-result");
        externalSTKPushRequest.setAccountReference(HelperUtility.getTransactionUniqueNumber());
        externalSTKPushRequest.setTransactionDesc(internalSTKPushRequest.getPhoneNumber() + " Transaction");

        AccessTokenResponse accessTokenResponse = getAccessToken();

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, Objects.requireNonNull(HelperUtility.toJson(externalSTKPushRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getStkPushRequestUrl())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            String responseBody = response.body().string();
            log.info(responseBody);
            return objectMapper.readValue(responseBody, STKPushSynchronousResponse.class);
        } catch (IOException e){
            log.error("Could not perform STK Push request -> %s");
            log.error(e.getLocalizedMessage());
            return null;
        }
    }
}
