package com.example.theshop.Model;

import java.util.List;

public class OrdersModelItem{
	private double amountPaid;
	private Integer orderId;
	private Integer paymentId;
	private String orderDate;
	private List<OrderedProductsItem> orderedProducts;

	public OrdersModelItem(int orderId, String orderDate, int paymentId, double amountPaid, List<OrderedProductsItem> orderedProducts) {
		this.orderId=orderId;
		this.orderDate=orderDate;
		this.paymentId=paymentId;
		this.amountPaid=amountPaid;
		this.orderedProducts=orderedProducts;
	}

	public void setAmountPaid(double amountPaid){
		this.amountPaid = amountPaid;
	}

	public double getAmountPaid(){
		return amountPaid;
	}

	public void setOrderId(Integer orderId){
		this.orderId = orderId;
	}

	public Integer getOrderId(){
		return orderId;
	}

	public void setPaymentId(Integer paymentId){
		this.paymentId = paymentId;
	}

	public Integer getPaymentId(){
		return paymentId;
	}

	public void setOrderDate(String orderDate){
		this.orderDate = orderDate;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public void setOrderedProducts(List<OrderedProductsItem> orderedProducts){
		this.orderedProducts = orderedProducts;
	}

	public List<OrderedProductsItem> getOrderedProducts(){
		return orderedProducts;
	}
}