package com.petsocity.orders.service;

import com.petsocity.orders.model.Order;
import com.petsocity.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    public Order createOrder(Order order) {
        return repository.save(order);
    }

    public List<Order> getAllOrders() {
    return repository.findAll();
    }

    public Optional<Order> getOrderByOrderId(String orderId) {
        return repository.findByOrderId(orderId);
    }
}
