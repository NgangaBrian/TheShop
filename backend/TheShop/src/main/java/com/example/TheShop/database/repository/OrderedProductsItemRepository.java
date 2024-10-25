package com.example.TheShop.database.repository;

import com.example.TheShop.darajaApi.dtos.ProductsItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductsItemRepository extends JpaRepository<ProductsItem, Long> {
}
