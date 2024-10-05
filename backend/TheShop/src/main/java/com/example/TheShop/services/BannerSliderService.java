package com.example.TheShop.services;

import com.example.TheShop.models.BannerSliderModel;
import com.example.TheShop.repository.BannerSliderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerSliderService {
    private final BannerSliderRepository bannerSliderRepository;
    public BannerSliderService(BannerSliderRepository bannerSliderRepository) {
        this.bannerSliderRepository = bannerSliderRepository;
    }

    public List<BannerSliderModel> getAllBannerSliders() {
        return bannerSliderRepository.findAll();
    }
}
