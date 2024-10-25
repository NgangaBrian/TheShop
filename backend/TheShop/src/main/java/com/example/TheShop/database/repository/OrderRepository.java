package com.example.TheShop.database.repository;

import com.example.TheShop.darajaApi.dtos.OrdersModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrdersModel, Long> {
}
