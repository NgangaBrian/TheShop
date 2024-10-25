package com.example.TheShop.darajaApi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ordered_products")
public class ProductsItem{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "orders_id")
	private OrdersModel orders;

	@JsonProperty("productId")
	@Column(name = "product_id")
	private int product_id;

	@JsonProperty("quantity")
	@Column(name = "quantity")
	private int quantity;

}