package com.pedro.aviationapi.application.ports;

import com.pedro.aviationapi.infrastructure.persistence.entities.AirportCacheEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AirportCachePort {
    Optional<AirportCacheEntity> findByCode(String code);
    Optional<AirportCacheEntity> findByFaaCodeOrIcaoCode(String code, String code2);
    void save(AirportCacheEntity airport);
    Optional<AirportCacheEntity> findByCodeCriteria(String city);
    void deleteAllExpired();
    Optional<AirportCacheEntity> findWithPlanesByFaaCodeOrIcaoCode(String code, String code2);
    List<AirportCacheEntity> findByExpiresAtBefore(LocalDateTime dateTime);
    void deleteAll(List<AirportCacheEntity> airportCacheEntities);
}
