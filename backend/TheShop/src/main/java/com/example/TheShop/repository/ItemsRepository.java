package com.example.TheShop.repository;

import com.example.TheShop.models.ItemsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<ItemsModel, Long> {
}
