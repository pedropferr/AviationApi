package com.pedro.aviationapi.application.ports;

import com.pedro.aviationapi.infrastructure.persistence.entities.AirportCacheEntity;

import java.util.Optional;

public interface AirportCachePort {
    Optional<AirportCacheEntity> findByCode(String code);
    Optional<AirportCacheEntity> findByFaaCodeOrIcaoCode(String code, String code2);
    AirportCacheEntity save(AirportCacheEntity airport);
    Optional<AirportCacheEntity> findByCodeCriteria(String city);
    void deleteAllExpired();
}
