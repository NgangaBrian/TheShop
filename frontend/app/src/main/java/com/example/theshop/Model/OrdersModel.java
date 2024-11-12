package com.example.theshop.Model;

import java.util.List;


public class OrdersModel{
	private List<OrdersModelItem> ordersModel;

	public OrdersModel(int orderId, String orderDate, int paymentId, double amountPaid) {
	}

	public void setOrdersModel(List<OrdersModelItem> ordersModel){
		this.ordersModel = ordersModel;
	}

	public List<OrdersModelItem> getOrdersModel(){
		return ordersModel;
	}
}