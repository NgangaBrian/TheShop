package com.example.TheShop.models;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class CategoriesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categories_id;
    private String name;
    private String image_url;

    public CategoriesModel() {}

    public CategoriesModel(Long categories_id, String name, String image_url) {
        this.categories_id = categories_id;
        this.name = name;
        this.image_url = image_url;
    }

    public Long getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(Long categories_id) {
        this.categories_id = categories_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
