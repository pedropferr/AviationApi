package com.pedro.aviationapi.application.services;

import com.pedro.aviationapi.application.ports.AirportCachePort;
import com.pedro.aviationapi.application.ports.AirportClientPort;
import com.pedro.aviationapi.infrastructure.persistence.entities.AirportCacheEntity;
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

    private final AirportClientPort aiportClient;
    private final AirportCachePort cacheAiportClient;

    public AirportService(AirportClientPort aiportClient, AirportCachePort cacheAiportClient) {
        this.aiportClient = aiportClient;
        this.cacheAiportClient = cacheAiportClient;
    }

    /**
     * Metodo que retorna o aeroporto pelo código, podendo ser utilizado o cache.
     *
     * @param code Código do aeroporto
     * @return Lista de AirportResponse
     */
    @Async
    @TimeLimiter(name = "externalService")
    @Retry(name = "externalService", fallbackMethod = "fallback")
    public CompletableFuture<List<AirportResponse>> getAirportsByCode(String code) {
        try {
            Optional<AirportCacheEntity> airportInCache = cacheAiportClient.findByCodeCriteria(code);

            if (airportInCache.isPresent()) {
                AirportCacheEntity entity = airportInCache.get();

                AirportResponse response = new AirportResponse(
                        entity.faaCode, entity.icaoCode, entity.name,
                        entity.city, entity.state, entity.country,
                        "CACHE"
                );

                return CompletableFuture.completedFuture(List.of(response));
            }

            List<AirportResponse> responses = aiportClient.fetchAirports(code);

            responses.forEach(r -> {
                AirportCacheEntity entity = new AirportCacheEntity(
                        r.faaCode, r.icaoCode, r.name,
                        r.city, r.state, r.country,
                        LocalDateTime.now().plusMinutes(5)
                );
                cacheAiportClient.save(entity);
            });

            return CompletableFuture.completedFuture(responses);

        } catch (Exception e) {
            CompletableFuture<List<AirportResponse>> failed = new CompletableFuture<>();
            failed.completeExceptionally(new RuntimeException("Erro ao buscar aeroportos", e));
            return failed;
        }
    }

    /**
     * Metodo chamado quando todas as tentativas falham
     */
    public CompletableFuture<List<AirportResponse>> fallback(String codes, Throwable t) {
        return CompletableFuture.completedFuture(List.of());
    }
}