package com.example.TheShop.darajaApi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterUrlResponse{

	@JsonProperty("ResponseCode")
	private String responseCode;

	@JsonProperty("ResponseDescription")
	private String responseDescription;

	@JsonProperty("OriginatorCoversationID")
	private String originatorCoversationID;

	/*
	{
            "OriginatorCoversationID": "8c39-4e24-ba78-a464bfcb7db629839",
            "ResponseCode": "0",
            "ResponseDescription": "Success"
        }
	 */
}