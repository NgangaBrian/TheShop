package com.example.TheShop.database.repository;

import com.example.TheShop.database.models.Payments;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payments, Long> {
}
