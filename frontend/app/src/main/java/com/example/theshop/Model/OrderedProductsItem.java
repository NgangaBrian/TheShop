package com.example.theshop.Model;

public class OrderedProductsItem{
	private Integer quantity;
	private Integer productId;
	private String imageUrl;
	private String productName;

	public OrderedProductsItem(int productId, int quantity, String productName, String imageUrl) {
	}

	public void setQuantity(Integer quantity){
		this.quantity = quantity;
	}

	public Integer getQuantity(){
		return quantity;
	}

	public void setProductId(Integer productId){
		this.productId = productId;
	}

	public Integer getProductId(){
		return productId;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}
}
