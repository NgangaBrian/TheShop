package com.example.TheShop.database.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "banners")
public class BannerSliderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long banners_id;

    @Setter
    @Getter
    @Column(name = "image_url")
    private String imageUrl;

    @Setter
    @Getter
    @Column(name = "name")
    private String bannerName;

    public BannerSliderModel(Long banners_id, String imageUrl, String bannerName) {
        this.banners_id = banners_id;
        this.imageUrl = imageUrl;
        this.bannerName = bannerName;
    }
    public BannerSliderModel(){}


    public void setId(Long banners_id) {
        this.banners_id = banners_id;
    }

    public Long getId() {
        return banners_id;
    }
}
