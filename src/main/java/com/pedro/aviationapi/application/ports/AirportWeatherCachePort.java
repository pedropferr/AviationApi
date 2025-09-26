package com.pedro.aviationapi.application.ports;

import com.pedro.aviationapi.infrastructure.persistence.entities.AirportWeatherCacheEntity;

import java.util.Optional;

public interface AirportWeatherCachePort {
    void save(AirportWeatherCacheEntity airport);
    Optional<AirportWeatherCacheEntity> findByCodeCriteria(String code);
    void deleteAllExpired();
}
