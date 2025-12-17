package com.petsocity.orders.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${meteored.api.key}")
    private String apiKey;

    @Value("${meteored.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        return headers;
    }

    // 1️⃣ Obtener ubicación (HASH) por coordenadas
    public String getLocationByCoords(double lat, double lon) {

        String url = String.format(
                "%s/api/location/v1/search/coords/%.7f/%.7f",
                baseUrl, lat, lon
        );

        HttpEntity<Void> entity = new HttpEntity<>(headers());

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
        );

        return response.getBody();
    }

    // 2️⃣ Forecast diario usando HASH
    public String getDailyForecastByHash(String hash) {

        if (hash == null || hash.isBlank()) {
            throw new IllegalArgumentException("Hash inválido o vacío");
        }

        String url = baseUrl + "/api/forecast/v1/daily/" + hash;

        HttpEntity<Void> entity = new HttpEntity<>(headers());

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
        );

        return response.getBody();
    }

}
