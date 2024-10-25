package com.example.TheShop.darajaApi.dtos;

import lombok.Data;

@Data
public class AcknowledgementRequest {
    private STKPushAsynchronousResponse stkPushAsynchronousResponse;
    private OrdersModel ordersModel;
}
