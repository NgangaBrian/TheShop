package com.example.TheShop.database.models;

import lombok.Data;

@Data
public class OrderedProductDTO {
    private Long productId;
    private int quantity;
    private String productName;
    private String imageUrl;
}
