package com.example.TheShop.darajaApi.dtos;

import com.example.TheShop.database.models.ItemsModel;
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

	@ManyToOne
	@JoinColumn(name = "product_id")
	private ItemsModel itemsModel;

	@JsonProperty("quantity")
	@Column(name = "quantity")
	private int quantity;

	@JsonProperty("productId")
	public void setProductId(Long productId) {
		// Custom logic to set ItemsModel based on the productId
		if (this.itemsModel == null) {
			this.itemsModel = new ItemsModel();
		}
		this.itemsModel.setProduct_id(productId);
	}

}