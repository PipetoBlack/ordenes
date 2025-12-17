package com.petsocity.orders.controller;

import com.petsocity.orders.service.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins = "*") // Solo para desarrollo
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Endpoint público para el frontend
     * Ejemplo:
     * /api/v1/weather/forecast?cityId=349727
     */
    @GetMapping("/forecast")
    public ResponseEntity<?> getForecast(@RequestParam String cityId) {

        try {
            String forecastJson = weatherService.getForecastByCityId(cityId);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(forecastJson);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "No se pudo obtener el pronóstico",
                            "detail", e.getMessage()
                    ));
        }
    }
}
