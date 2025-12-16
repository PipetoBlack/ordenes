package com.petsocity.orders.service;

import org.springframework.beans.factory.annotation.Value;
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

    public String getLocationByCoords(double lat, double lon) {

        // ✅ Endpoint gratuito de Meteored
        String url = String.format(
            "%s/api/location/v1/search/coords/%.7f/%.7f?key=%s",
            meteoredBaseUrl,
            lat,
            lon,
            meteoredApiKey
        );

        System.out.println("URL Meteored (Location): " + url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (Exception e) {
            System.out.println("Error llamando a Meteored: " + e.getMessage());
            throw new RuntimeException("Error al obtener ubicación desde Meteored");
        }
    }
}
