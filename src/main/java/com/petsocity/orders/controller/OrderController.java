//Controller
package com.petsocity.orders.controller;

import com.petsocity.orders.model.Order;
import com.petsocity.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

@PostMapping
public ResponseEntity<?> createOrder(@RequestBody Order order) {
    try {
        Order saved = service.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    } catch (Exception e) {
        e.printStackTrace();
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", "Error al crear la orden");
        errorBody.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody);
    }
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

@PostMapping("/webhook")
public ResponseEntity<?> webhook(@RequestBody Map<String, Object> payload) {
    try {
        // Obtén el código de la orden
        String orderCode = (String) payload.get("commerceOrder");

        // Mercado Pago puede mandar "status" o "collection_status"
        String status = null;
        if (payload.get("status") != null) {
            status = payload.get("status").toString();
        } else if (payload.get("collection_status") != null) {
            status = payload.get("collection_status").toString();
        }

        if ("approved".equalsIgnoreCase(status)) {
            service.markAsPaid(orderCode);
        } else {
            System.out.println("Webhook recibido, status: " + status + ", orderCode: " + orderCode);
        }

        // Siempre responder con un JSON consistente
        return ResponseEntity.ok(Map.of("received", true));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage(), "received", false));
    }
}


}
