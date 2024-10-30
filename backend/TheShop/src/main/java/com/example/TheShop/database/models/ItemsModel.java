package com.example.TheShop.database.models;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class ItemsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long products_id;
    private String product_name;
    private String product_description;
    private String image_url;
    private Double product_price;
    private int subcategory_id;

    public ItemsModel() {
    }

    @Override
    public String toString() {
        return "ItemsModel [products_id=" + products_id + ", product_name=" + product_name +
                ", product_description=" + product_description + ", image_url=" + image_url +
                ", product_price=" + product_price + ", subcategory_id=" + subcategory_id + "]";
    }

    public ItemsModel(Long products_id, String product_name, String product_description, String image_url, Double product_price, int subcategory_id) {
        this.products_id = products_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.image_url = image_url;
        this.product_price = product_price;
        this.subcategory_id = subcategory_id;
    }

    public Long getProduct_id() {
        return products_id;
    }

    public void setProduct_id(Long products_id) {
        this.products_id = products_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Double product_price) {
        this.product_price = product_price;
    }

    public int getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(int subcategory_id) {
        this.subcategory_id = subcategory_id;
    }
}
