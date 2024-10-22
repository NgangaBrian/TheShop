package com.example.TheShop.database.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "payments")
public class Payments {

    @Id
    private Long id;
    private Long userId;
    private String transactionId;
    private LocalDateTime transactionTime;
    private String phoneNumber;
    private BigDecimal amount;
    private String merchantRequestID;
    private String checkoutRequestID;
    private int resultCode;
    private String resultDesc;
    private String firstName;
    private String middleName;
    private String lastName;

}
