package com.petsocity.orders.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
            System.out.println("Respuesta Meteored: " + response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error HTTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Error HTTP al obtener ubicación");
        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
            throw new RuntimeException("Error general al obtener ubicación");
        }

    }
}
