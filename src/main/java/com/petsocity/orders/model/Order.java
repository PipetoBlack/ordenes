package com.petsocity.orders.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private String orderNumber;
    private String orderCode;

    @Column(columnDefinition = "TEXT")
    private String customerData;

    @Column(columnDefinition = "TEXT")
    private String cartItems;

    @Column(columnDefinition = "TEXT")
    private String totals;

    private String deliveryMethod;

    private LocalDateTime createdAt = LocalDateTime.now();
}
