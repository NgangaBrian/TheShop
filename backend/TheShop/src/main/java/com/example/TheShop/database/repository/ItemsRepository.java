package com.example.TheShop.database.repository;

import com.example.TheShop.database.models.ItemsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<ItemsModel, Long> {
    Page<ItemsModel> findAll(Pageable pageable);
}
