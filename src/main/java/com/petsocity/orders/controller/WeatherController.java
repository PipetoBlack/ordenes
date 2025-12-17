package com.petsocity.orders.controller;

import com.petsocity.orders.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") // solo dev
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentWeather() {
        return ResponseEntity.ok(
                weatherService.getCurrentWeatherSantiago()
        );
    }
}
