package com.pedro.aviationapi.application.services;

import com.pedro.aviationapi.application.ports.AirportCachePort;
import com.pedro.aviationapi.application.ports.AirportWeatherCachePort;
import com.pedro.aviationapi.infrastructure.persistence.entities.AirportCacheEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CacheCleanupService {

    private final AirportCachePort cacheAiportClient;
    private final AirportWeatherCachePort cacheAiportWeatherClient;

    public CacheCleanupService(AirportCachePort cacheAiportClient, AirportWeatherCachePort cacheAiportWeatherClient) {
        this.cacheAiportClient = cacheAiportClient;
        this.cacheAiportWeatherClient = cacheAiportWeatherClient;
    }

    // Roda a cada 5 minutos
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void cleanUpExpiredCache() {
        List<AirportCacheEntity> expiredAirports =
                cacheAiportClient.findByExpiresAtBefore(LocalDateTime.now());

        if (!expiredAirports.isEmpty())
            cacheAiportClient.deleteAll(expiredAirports);
    }

    // Roda a cada 5 minutos
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void cleanUpExpiredWeatherCache() {
        cacheAiportWeatherClient.deleteAllExpired();
    }
}
