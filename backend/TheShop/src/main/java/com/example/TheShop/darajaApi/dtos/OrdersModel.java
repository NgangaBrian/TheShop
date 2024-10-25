package com.example.TheShop.darajaApi.dtos;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrdersModel{

	@Id
	@JsonProperty("id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonProperty("paymentId")
	private Long payment_id;

	@JsonProperty("customerId")
	private Long customer_id;

	@JsonProperty("orderDate")
	private Date order_date;

    @JsonProperty("products")
	@OneToMany(mappedBy = "orders", cascade = CascadeType.PERSIST)
	private List<ProductsItem> products;
}