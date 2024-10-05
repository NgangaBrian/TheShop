package com.example.theshop.Model;

public class SliderModel {
    private Long id;
    private String name;
    private String imageUrl;

    public SliderModel(Long id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
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

    public SliderModel(){}



    public String getimageUrl() {
        return imageUrl;
    }

    public void setimageUrl(String url) {
        this.imageUrl = url;
    }
}
