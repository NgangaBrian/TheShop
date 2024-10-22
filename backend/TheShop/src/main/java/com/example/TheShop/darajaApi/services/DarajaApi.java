package com.example.TheShop.darajaApi.services;

import com.example.TheShop.darajaApi.dtos.*;

public interface DarajaApi {
    /**
     * Returns Daraja Api Access Token Response
     */
    AccessTokenResponse getAccessToken();

    RegisterUrlResponse registerUrl();

    STKPushSynchronousResponse performSTKPushRequest(InternalSTKPushRequest internalSTKPushRequest);
}
