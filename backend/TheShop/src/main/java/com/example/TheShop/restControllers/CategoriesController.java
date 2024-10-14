package com.example.TheShop.restControllers;


import com.example.TheShop.models.CategoriesModel;
import com.example.TheShop.services.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("/categories")
    public List<CategoriesModel> getAllCategories() {
        return categoriesService.getAllCategories();
    }

    // todo :
}
