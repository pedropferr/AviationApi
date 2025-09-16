package com.pedro.aviationapi.application.services;

import static com.pedro.aviationapi.shared.validation.ValidationUtils.validateAirportsCodes;
import com.pedro.aviationapi.application.interfaces.AirportClientPort;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import com.pedro.aviationapi.api.dtos.AirportResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.List;

@Service
public class AirportService {

    private final AirportClientPort client;

    public AirportService(AirportClientPort client) {
        this.client = client;
    }

    /**
     * Metodo que retorna os aeroporto(s) pelo(s) código(s).
     *
     * @param codes Códigos separados por vírgula
     * @return Lista de AirportResponse
     */
    @TimeLimiter(name = "externalService")
    @Retry(name = "externalService", fallbackMethod = "fallback")
    public CompletableFuture<List<AirportResponse>> getAirportsByCode(String codes) {
        validateAirportsCodes(codes);
        return client.fetchAirports(codes);
    }

    /**
     * Metodo chamado quando todas as tentativas falham
     */
    public CompletableFuture<List<AirportResponse>> fallback(String codes, Throwable t) {
        return CompletableFuture.completedFuture(List.of());
    }
}