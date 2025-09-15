package com.pedro.aviationapi.application.interfaces;

import com.pedro.aviationapi.shared.dtos.AirportResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AirportClientPort {
    CompletableFuture<List<AirportResponse>> fetchAirports(String code);
}
