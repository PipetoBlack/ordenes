package com.petsocity.orders.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.petsocity.orders.model.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentClient {

    private final RestTemplate restTemplate;

    @Value("${payments.api.url}")
    private String paymentsApiUrl;

    public void createPayment(Order order) {

        Map<String, Object> body = Map.of(
            "orderCode", order.getOrderCode(),
            "amount", 15000,
            "email", "cliente@correo.cl"
        );

        restTemplate.postForEntity(
            paymentsApiUrl + "/api/v1/payments",
            body,
            Void.class
        );
    }
}
