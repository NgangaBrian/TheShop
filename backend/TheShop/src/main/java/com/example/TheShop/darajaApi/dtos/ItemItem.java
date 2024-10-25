package com.example.TheShop.darajaApi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItemItem{

	@JsonProperty("Value")
	private String value;

	@JsonProperty("Name")
	private String name;

	public ItemItem() {}
	public ItemItem(String value, String name) {
		this.name = name;
		this.value = value;
	}
}