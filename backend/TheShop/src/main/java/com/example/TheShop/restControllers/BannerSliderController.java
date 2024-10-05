package com.example.TheShop.restControllers;

import com.example.TheShop.models.BannerSliderModel;
import com.example.TheShop.repository.BannerSliderRepository;
import com.example.TheShop.services.BannerSliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BannerSliderController {

    private final BannerSliderService bannerSliderService;

    public BannerSliderController(BannerSliderService bannerSliderService) {
        this.bannerSliderService = bannerSliderService;
    }

    @GetMapping("/bannersliders")
    public List<BannerSliderModel> getSliders() {
        return bannerSliderService.getAllBannerSliders();
    }

}
