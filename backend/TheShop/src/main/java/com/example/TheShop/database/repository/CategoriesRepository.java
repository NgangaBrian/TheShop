package com.example.TheShop.database.repository;

import com.example.TheShop.database.models.CategoriesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<CategoriesModel, Long> {
}
