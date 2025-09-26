package com.pedro.aviationapi.application.services;

import com.pedro.aviationapi.api.dtos.WeatherResponse;
import com.pedro.aviationapi.application.ports.AirportCachePort;
import com.pedro.aviationapi.application.ports.AirportClientPort;
import com.pedro.aviationapi.application.ports.AirportWeatherCachePort;
import com.pedro.aviationapi.infrastructure.persistence.entities.AirportCacheEntity;
import com.pedro.aviationapi.infrastructure.persistence.entities.AirportWeatherCacheEntity;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import com.pedro.aviationapi.api.dtos.AirportResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.List;

@Service
public class AirportService {

    private final AirportClientPort aiportPort;
    private final AirportCachePort cacheAiportPort;
    private final AirportWeatherCachePort cacheAiportWeatherPort;

    public AirportService(AirportClientPort aiportPort, AirportCachePort cacheAiportPort, AirportWeatherCachePort cacheAiportWeatherPort) {
        this.aiportPort = aiportPort;
        this.cacheAiportPort = cacheAiportPort;
        this.cacheAiportWeatherPort = cacheAiportWeatherPort;
    }

    /**
     * Metodo que retorna o aeroporto pelo código, podendo ser utilizado o cache.
     *
     * @param code Código do aeroporto
     * @return Lista de AirportResponse
     */
    @Retry(name = "externalService", fallbackMethod = "fallback")
    public AirportResponse getAirportsByCode(String code) {
        try {
            AirportResponse response = getAirportInfo(code);
            response.weather = getAirportWeatherInfo(code);

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AirportResponse getAirportInfo(String code) {
        try {
            Optional<AirportCacheEntity> airportInCache = cacheAiportPort.findByCodeCriteria(code);

            if (airportInCache.isPresent()) {
                AirportCacheEntity entity = airportInCache.get();

                return new AirportResponse(
                        entity.faaCode, entity.icaoCode, entity.name,
                        entity.city, entity.state, entity.country,
                        "CACHE"
                );
            }

            AirportResponse response = aiportPort.fetchAirport(code);

            if (!response.success) return response;

            AirportCacheEntity entity = new AirportCacheEntity(
                    response.faaCode, response.icaoCode, response.name,
                    response.city, response.state, response.country,
                    LocalDateTime.now().plusMinutes(5)
            );
            cacheAiportPort.save(entity);

            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private WeatherResponse getAirportWeatherInfo(String code) {
        try {
            Optional<AirportWeatherCacheEntity> weatherInCache = cacheAiportWeatherPort.findByCodeCriteria(code);

            if (weatherInCache.isPresent()) {
                AirportWeatherCacheEntity entity = weatherInCache.get();

                return new WeatherResponse(
                        entity.temperature, entity.wind, entity.visibility,"CACHE"
                );
            }

            WeatherResponse response = aiportPort.fetchAirportWeather(code);

            if (!response.success) return response;

            AirportWeatherCacheEntity entity = new AirportWeatherCacheEntity(
                    code, response.temperature, response.wind,
                    response.visibility, LocalDateTime.now().plusMinutes(5)
            );
            cacheAiportWeatherPort.save(entity);

            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Metodo chamado quando todas as tentativas falham
     */
    public CompletableFuture<AirportResponse> fallback(String code, Throwable ex) {
        AirportResponse response = new AirportResponse();
        response.faaCode = code;
        response.source = "FALLBACK";
        response.name = "Não disponível";
        return CompletableFuture.completedFuture(response);
    }
}