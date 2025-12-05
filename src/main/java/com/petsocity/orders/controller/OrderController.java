package com.petsocity.orders.controller;

import com.petsocity.orders.model.Order;
import com.petsocity.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {

        // ✅ Generar ID único basado en timestamp
        long generatedId = System.currentTimeMillis();

        order.setOrderId(generatedId);
        order.setOrderNumber("#" + generatedId);
        order.setOrderCode("ORDER" + generatedId);

        Order saved = service.createOrder(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        return service.getOrderByOrderId(orderId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Orden no encontrada")));
    }
}
