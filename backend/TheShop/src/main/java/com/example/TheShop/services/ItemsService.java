package com.example.TheShop.services;

import com.example.TheShop.models.ItemsModel;
import com.example.TheShop.repository.ItemsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class ItemsService {
    public final ItemsRepository itemsRepository;
    public ItemsService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    public List<ItemsModel> getAllItems() {
        return itemsRepository.findAll();
    }

}
