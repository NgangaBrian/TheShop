package com.example.TheShop.models;

import jakarta.persistence.*;

@Entity
@Table(name = "banners")
public class BannerSliderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long banners_id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "name")
    private String bannerName;

    public BannerSliderModel(Long banners_id, String imageUrl, String bannerName) {
        this.banners_id = banners_id;
        this.imageUrl = imageUrl;
        this.bannerName = bannerName;
    }
    public BannerSliderModel(){}


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public void setId(Long banners_id) {
        this.banners_id = banners_id;
    }

    public Long getId() {
        return banners_id;
    }
}
