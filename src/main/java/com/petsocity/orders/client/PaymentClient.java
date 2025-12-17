//client/paymentClient

package com.petsocity.orders.client;

import com.petsocity.orders.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentClient {

    private final RestTemplate restTemplate;

    @Value("${payments.api.url}")
    private String paymentsApiUrl; // microservicio Payments

    public String createPayment(Order order) {
        // Preparar payload
        Map<String, Object> body = Map.of(
                "commerceOrder", order.getOrderCode(),
                "amount", extractTotal(order.getTotals()),
                "subject", "Compra PetSocity",
                "email", extractEmail(order.getCustomerData()),
                "urlReturn", "https://petsocity.vercel.app/compraExitosa?orderId=" + order.getOrderId(),
                "urlConfirmation", "https://orders-petsocity.up.railway.app/api/v1/orders/webhook"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                paymentsApiUrl + "/api/v1/payments/create", request, Map.class
        );

        Map<String, Object> data = response.getBody();

        if (data == null || !data.containsKey("url")) {
            throw new RuntimeException("Respuesta inválida desde Payments API: " + data);
        }

        return (String) data.get("url"); // URL de Mercado Pago (init_point)
    }

    private Integer extractTotal(String totalsJson) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> totals = mapper.readValue(totalsJson, Map.class);
            return (Integer) totals.get("total");
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo total de totals JSON", e);
        }
    }

    private String extractEmail(String customerDataJson) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> customerData = mapper.readValue(customerDataJson, Map.class);
            return (String) customerData.get("correo");
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo correo del customerData", e);
        }
    }
}
