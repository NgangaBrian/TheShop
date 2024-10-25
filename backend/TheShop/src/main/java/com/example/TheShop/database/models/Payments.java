package com.example.TheShop.database.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long user_id;
    private String trans_id;
    private LocalDateTime trans_time;
    private String phone_number;
    private BigDecimal amount;
    private String marchant_request_id;
    private String checkout_request_id;
    private int result_code;
    private String result_desc;
    private String first_name;
    private String middle_name;
    private String last_name;

}
