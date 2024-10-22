package com.example.theshop.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsModel implements Serializable {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private int categoryId;
    private ArrayList<String> size;
    private double rating;
    private  int numberInCart;

    public ItemsModel() {}

    public ItemsModel(Long id, String name, String description, String imageUrl, ArrayList<String> size, double price, double rating, int numberInCart, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.numberInCart = numberInCart;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }



    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}
