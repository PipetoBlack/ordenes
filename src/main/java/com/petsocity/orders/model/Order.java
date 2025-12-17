//model
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

    // Identificador público de la orden
    @Column(nullable = false, unique = true)
    private Long orderId;

    private String orderNumber;
    private String orderCode;

    @Column(columnDefinition = "TEXT")
    private String customerData;

    @Column(columnDefinition = "TEXT")
    private String cartItems;

    @Column(columnDefinition = "TEXT")
    private String totals;

    private String deliveryMethod;

    @Column(nullable = false)
    private String status; // CREATED, PENDING_PAYMENT, PAID, REJECTED

    @Column(columnDefinition = "TEXT")
    private String paymentUrl;

    private LocalDateTime createdAt = LocalDateTime.now();
}
