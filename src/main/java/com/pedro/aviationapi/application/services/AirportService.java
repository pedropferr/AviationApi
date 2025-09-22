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

    private final AirportClientPort aiportPort;
    private final AirportCachePort cacheAiportPort;

    public AirportService(AirportClientPort aiportPort, AirportCachePort cacheAiportPort) {
        this.aiportPort = aiportPort;
        this.cacheAiportPort = cacheAiportPort;
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
            Optional<AirportCacheEntity> airportInCache = cacheAiportPort.findByCodeCriteria(code);

            if (airportInCache.isPresent()) {
                AirportCacheEntity entity = airportInCache.get();

                AirportResponse response = new AirportResponse(
                        entity.faaCode, entity.icaoCode, entity.name,
                        entity.city, entity.state, entity.country,
                        "CACHE"
                );

                return CompletableFuture.completedFuture(List.of(response));
            }

            List<AirportResponse> responses = aiportPort.fetchAirports(code);

            responses.forEach(r -> {
                AirportCacheEntity entity = new AirportCacheEntity(
                        r.faaCode, r.icaoCode, r.name,
                        r.city, r.state, r.country,
                        LocalDateTime.now().plusMinutes(5)
                );
                cacheAiportPort.save(entity);
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