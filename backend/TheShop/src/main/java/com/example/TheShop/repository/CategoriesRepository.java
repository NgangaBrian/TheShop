package com.example.TheShop.repository;

import com.example.TheShop.models.CategoriesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<CategoriesModel, Long> {
}
