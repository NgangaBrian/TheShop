package com.example.TheShop.darajaApi.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AcknowledgeResponseForOrders{

	@JsonProperty("ordersModel")
	private OrdersModel ordersModel;

	@JsonProperty("stkPushAsynchronousResponse")
	private STKPushAsynchronousResponse stkPushAsynchronousResponse;
}