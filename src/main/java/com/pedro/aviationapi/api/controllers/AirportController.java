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
     * Busca um aeroportos pelo código FAA ou ICAO.
     * Exemplo de chamada: GET /api/aeroportos/consultar
     * body:
     * {
     * 	"airportCode": "AVL"
     * }
     * @param request Código do aéroporto a ser consultado
     * @return Lista de AirportResponse
     */
    @PostMapping("/consultar")
    public CompletableFuture<List<AirportResponse>> getAirport(@Valid @RequestBody AirportRequest request) {
        return airportService.getAirportsByCode(request.airportCode);
    }
}