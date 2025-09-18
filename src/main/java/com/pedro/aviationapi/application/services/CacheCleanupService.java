package com.pedro.aviationapi.application.services;

import com.pedro.aviationapi.application.ports.AirportCachePort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CacheCleanupService {

    private final AirportCachePort cacheAiportClient;

    public CacheCleanupService(AirportCachePort cacheAiportClient) {
        this.cacheAiportClient = cacheAiportClient;
    }

    // Roda a cada 5 minutos
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void cleanUpExpiredCache() {
        cacheAiportClient.deleteAllExpired();
    }
}
