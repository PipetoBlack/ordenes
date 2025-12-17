package com.petsocity.orders.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.petsocity.orders.dto.CustomerDataDTO;
import com.petsocity.orders.dto.TotalsDTO;


import com.petsocity.orders.model.Order;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class PaymentClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${payments.api.url}")
    private String paymentsApiUrl;

    public String createPayment(Order order) {

        CustomerDataDTO customer =
            objectMapper.readValue(order.getCustomerData(), CustomerDataDTO.class);
        TotalsDTO totals =
            objectMapper.readValue(order.getTotals(), TotalsDTO.class);

        Map<String, Object> body = Map.of(
            "commerceOrder", order.getOrderCode(),
            "amount", totals.getTotal(),
            "subject", "Compra PetSocity",
            "email", customer.getCorreo(),
            "urlConfirmation", "https://payments-payment.up.railway.app/api/v1/payments/webhook",
            "urlReturn", "https://petsocity.vercel.app/compraExitosa?orderId=" + order.getOrderId()
        );

        ResponseEntity<Map> response =
            restTemplate.postForEntity(
                paymentsApiUrl + "/api/v1/payments",
                body,
                Map.class
            );

        Map<String, Object> data = response.getBody();

        if (data == null || !data.containsKey("url") || !data.containsKey("token")) {
            throw new RuntimeException("Respuesta inválida desde Payments: " + data);
        }

        Object urlObj = data.get("url");
        Object tokenObj = data.get("token");

        if (!(urlObj instanceof String) || !(tokenObj instanceof String)) {
            throw new RuntimeException("Formato inválido de URL/token desde Payments");
        }

        return UriComponentsBuilder
                .fromUriString((String) urlObj)
                .queryParam("token", tokenObj)
                .toUriString();
    }
}
