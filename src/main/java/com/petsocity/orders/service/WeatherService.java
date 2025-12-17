package com.petsocity.orders.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${meteored.api.key}")
    private String meteoredApiKey;

    @Value("${meteored.base.url}")
    private String meteoredBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Endpoint compatible con plan gratuito de Meteored
     */
    public String getForecastByCityId(String cityId) {

        String url = meteoredBaseUrl + "/api/forecast/" + cityId + "?language=es";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", meteoredApiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        System.out.println("Meteored URL: " + url);
        System.out.println("Meteored API KEY (header): OK");

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}
