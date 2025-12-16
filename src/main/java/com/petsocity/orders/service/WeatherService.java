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

    public String getWeatherByCoords(double lat, double lon) {
        String url = String.format(
            "%s/api/forecast/v1/daily/coords/%.7f/%.7f/0/America/Santiago?key=%s",
            meteoredBaseUrl, lat, lon, meteoredApiKey
        );

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}