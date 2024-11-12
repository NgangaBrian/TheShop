package com.example.TheShop.database.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Long orderId;
    private String orderDate;
    private Long paymentId;
    private BigDecimal amountPaid;
    private List<OrderedProductDTO> orderedProducts;
}
