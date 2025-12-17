//service
package com.petsocity.orders.service;

import com.petsocity.orders.client.PaymentClient;
import com.petsocity.orders.model.Order;
import com.petsocity.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository repository;
    private final PaymentClient paymentClient;

    public Order createOrder(Order order) {
        try {
            // Generar ID y códigos de orden
            long generatedId = System.currentTimeMillis();
            order.setOrderId(generatedId);
            order.setOrderNumber("#" + generatedId);
            order.setOrderCode("ORDER" + generatedId);
            order.setStatus("PENDING_PAYMENT");
            order.setCreatedAt(LocalDateTime.now());

            log.info("Guardando orden: {}", order);

            // Guardar orden inicialmente
            Order saved = repository.save(order);

            try {
                // LLAMADA AL MICROSERVICIO DE PAYMENTS (que ahora usa Mercado Pago)
                String paymentUrl = paymentClient.createPayment(saved);

                // Guardar URL de checkout
                saved.setPaymentUrl(paymentUrl);
                saved.setStatus("PENDING_PAYMENT");
                repository.save(saved);

                log.info("Pago creado correctamente para orden {}", saved.getOrderCode());

            } catch (Exception e) {
                log.error("Error creando pago para orden {}", saved.getOrderCode(), e);
                saved.setStatus("PAYMENT_ERROR");
                repository.save(saved);
            }

            return saved;

        } catch (Exception e) {
            log.error("Error grave al crear orden", e);
            throw new RuntimeException("No se pudo crear la orden");
        }
    }

    public void markAsPaid(String orderCode) {
        repository.findByOrderCode(orderCode).ifPresent(o -> {
            o.setStatus("PAID");
            repository.save(o);
        });
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Optional<Order> getOrderByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }
}
