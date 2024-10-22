package com.example.TheShop.darajaApi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class STKPushAsynchronousResponse{

	@JsonProperty("Body")
	private Body body;
}