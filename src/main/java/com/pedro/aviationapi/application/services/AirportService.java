package com.pedro.aviationapi.application.services;

import com.pedro.aviationapi.api.dtos.PlaneResponse;
import com.pedro.aviationapi.api.dtos.WeatherResponse;
import com.pedro.aviationapi.application.ports.AirportCachePort;
import com.pedro.aviationapi.application.ports.AirportClientPort;
import com.pedro.aviationapi.application.ports.AirportWeatherCachePort;
import com.pedro.aviationapi.domain.model.Airport;
import com.pedro.aviationapi.domain.model.Weather;
import com.pedro.aviationapi.infrastructure.persistence.entities.AirportCacheEntity;
import com.pedro.aviationapi.infrastructure.persistence.entities.AirportWeatherCacheEntity;
import com.pedro.aviationapi.api.dtos.AirportResponse;
import com.pedro.aviationapi.infrastructure.persistence.entities.PlaneEntity;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pedro.aviationapi.shared.util.PlaneFactory.generatePlanes;

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
            response.setWeather(getAirportWeatherInfo(code));

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AirportResponse getAirportInfo(String code) {
        try {
            Optional<AirportCacheEntity> teste = cacheAiportPort.findByCode(code);
            Optional<AirportCacheEntity> airportInCache = cacheAiportPort.findByCodeCriteria(code);

            if (airportInCache.isPresent()) {
                AirportCacheEntity entity = airportInCache.get();

                var response = AirportResponse.builder()
                        .faaCode(entity.getFaaCode())
                        .icaoCode(entity.getIcaoCode())
                        .name(entity.getName())
                        .city(entity.getCity())
                        .state(entity.getState())
                        .country(entity.getCountry())
                        .source("CACHE")
                        .build();

                response.setPlanes(buildPLanesResponse(entity.getPlanes()));

                return response;
            }

            Airport airport = aiportPort.fetchAirport(code);
            AirportResponse response = AirportResponse.builder()
                    .faaCode(airport.getFaaCode())
                    .icaoCode(airport.getIcaoCode())
                    .name(airport.getName())
                    .city(airport.getCity())
                    .state(airport.getState())
                    .country(airport.getCountry())
                    .source(airport.getSource())
                    .build();

            if (!airport.getSuccess()) return response;

            AirportCacheEntity entity = AirportCacheEntity.builder()
                    .faaCode(response.getFaaCode())
                    .icaoCode(response.getIcaoCode())
                    .name(response.getName())
                    .city(response.getCity())
                    .state(response.getState())
                    .country(response.getCountry())
                    .expiresAt(LocalDateTime.now().plusMinutes(5))
                    .build();

            entity.setPlanes(generatePlanes());

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

                return WeatherResponse.builder()
                        .temperature(entity.getTemperature())
                        .wind(entity.getWind())
                        .visibility(entity.getVisibility())
                        .source("CACHE")
                        .build();
            }

            Weather weather = aiportPort.fetchAirportWeather(code);
            WeatherResponse response = WeatherResponse.builder()
                    .temperature(weather.getTemperature())
                    .wind(weather.getWind())
                    .visibility(weather.getVisibility())
                    .source(weather.getSource())
                    .build();

            if (!weather.getSuccess()) return response;

            AirportWeatherCacheEntity entity = AirportWeatherCacheEntity.builder()
                    .stationId(code)
                    .temperature(response.getTemperature())
                    .wind(response.getWind())
                    .visibility(response.getVisibility())
                    .expiresAt(LocalDateTime.now().plusMinutes(5))
                    .build();

            cacheAiportWeatherPort.save(entity);

            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<PlaneResponse> buildPLanesResponse(List<PlaneEntity> planes) {
        List<PlaneResponse> dtos = new ArrayList<>();

        for (PlaneEntity plane : planes) {
            PlaneResponse dto = PlaneResponse.builder()
                    .registration(plane.getRegistration())
                    .model(plane.getModel())
                    .manufacturer(plane.getManufacturer())
                    .yearManufacture(plane.getYearManufacture())
                    .build();

            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * Metodo chamado quando todas as tentativas falham
     */
    public AirportResponse fallback(String code, Throwable ex) {
        return AirportResponse.builder()
                .faaCode(code)
                .source("FALLBACK")
                .name("Não disponível").build();
    }
}