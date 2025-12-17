package com.petsocity.orders.service;

import org.springframework.beans.factory.annotation.Value;
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
     * Obtiene el pronóstico del tiempo usando CITY ID
     * Compatible con plan gratuito de Meteored
     */
    public String getForecastByCityId(String cityId) {

        String url = String.format(
                "%s/api/forecast/%s?key=%s&language=es",
                meteoredBaseUrl,
                cityId,
                meteoredApiKey
        );

        System.out.println("Meteored URL: " + url);

        return restTemplate.getForObject(url, String.class);
    }
}
