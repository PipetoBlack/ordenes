package com.petsocity.orders.controller;

import com.petsocity.orders.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // Paso 1: obtener ubicación (HASH)
    @GetMapping("/location")
    public ResponseEntity<?> getLocation(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        return ResponseEntity.ok(weatherService.getLocationByCoords(lat, lon));
    }

    // Paso 2: obtener forecast con HASH
    @GetMapping("/forecast/daily")
    public ResponseEntity<?> getForecast(@RequestParam String hash) {
        try {
            return ResponseEntity.ok(weatherService.getDailyForecastByHash(hash));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error obteniendo pronóstico"));
        }
    }

}
