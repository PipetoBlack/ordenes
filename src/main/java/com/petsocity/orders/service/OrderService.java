package com.petsocity.orders.service;

import com.petsocity.orders.model.Order;
import com.petsocity.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    public Order createOrder(Order order) {

        // ✅ Generar un ID único basado en timestamp
        long generatedId = System.currentTimeMillis();

        order.setOrderId(generatedId);
        order.setOrderNumber("#" + generatedId);
        order.setOrderCode("ORDER" + generatedId);
        order.setCreatedAt(LocalDateTime.now());

        return repository.save(order);
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    // ✅ Ahora debe buscar por Long, no String
    public Optional<Order> getOrderByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }
}
