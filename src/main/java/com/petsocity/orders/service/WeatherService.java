package com.petsocity.orders.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Clima actual desde estación SCQN (Santiago)
     */
    public Map<String, Object> getCurrentWeatherSantiago() {

        String url = "https://api.gael.cloud/general/public/clima/SCQN";

        return restTemplate.getForObject(url, Map.class);
    }
}
