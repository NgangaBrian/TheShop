package com.example.TheShop.restControllers;

import com.example.TheShop.models.ItemsModel;
import com.example.TheShop.repository.ItemsRepository;
import com.example.TheShop.services.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping("/items")
    public List<ItemsModel> getAllItems() {
        return itemsService.getAllItems();
    }
}
