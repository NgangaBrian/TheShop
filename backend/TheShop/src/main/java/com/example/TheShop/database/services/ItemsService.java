package com.example.TheShop.database.services;

import com.example.TheShop.database.models.ItemsModel;
import com.example.TheShop.database.repository.ItemsRepository;
import org.springframework.stereotype.Service;

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
