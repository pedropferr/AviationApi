package com.pedro.aviationapi.api.controllers;

import com.pedro.aviationapi.api.dtos.AirportRequest;
import com.pedro.aviationapi.application.services.AirportService;
import com.pedro.aviationapi.api.dtos.AirportResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/aeroportos")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    /**
     * Busca um ou mais aeroportos pelo(s) código(s).
     * Exemplo de chamada: GET /api/aeroportos/AVL,KAVL
     *
     * @param codes Códigos separados por vírgula
     * @return Lista de AirportResponse
     */
    @GetMapping("/{codes}")
    public CompletableFuture<ResponseEntity<List<AirportResponse>>> searchAirports(@PathVariable String codes) {
        return airportService.getAirportsByCode(codes)
                .thenApply(list -> {
                    if (list.isEmpty())
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    return ResponseEntity.ok(list);
                });
    }

    @GetMapping("/consultar")
    public CompletableFuture<ResponseEntity<List<AirportResponse>>> getAirport(@Valid @RequestBody AirportRequest request) {
        return airportService.getAirportsByCode(request.faaCode)
                .thenApply(list -> {
                    if (list.isEmpty())
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    return ResponseEntity.ok(list);
                });
    }
}