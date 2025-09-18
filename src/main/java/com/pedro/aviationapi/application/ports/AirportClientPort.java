package com.pedro.aviationapi.application.ports;

import com.pedro.aviationapi.api.dtos.AirportResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AirportClientPort {
    List<AirportResponse> fetchAirports(String code);
}
