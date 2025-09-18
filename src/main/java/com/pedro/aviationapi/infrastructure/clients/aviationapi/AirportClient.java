package com.pedro.aviationapi.infrastructure.clients.aviationapi;

import com.pedro.aviationapi.application.ports.AirportClientPort;
import com.pedro.aviationapi.api.dtos.AirportResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class AirportClient implements AirportClientPort {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://api.aviationapi.com/";

    /**
     * Busca um aeroporto por código na api <a href="https://aviationapi.com/">...</a>
     *
     * @param codes Códigos IATA ou ICAO do aeroporto
     * @return AirportResponse com os dados do aeroporto
     * @throws RuntimeException Se houver erro ao consultar a API externa
     */
    @Override
    public List<AirportResponse> fetchAirports(String codes) {
        try {
            String url = BASE_URL + "v1/airports?apt=" + codes.trim();

            ResponseEntity<Map<String, List<AviationApiAirportResponse>>> responseEntity =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
                    );

            Map<String, List<AviationApiAirportResponse>> airportsMap = responseEntity.getBody();

            if (airportsMap == null || airportsMap.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum aeroporto encontrado");

            List<AirportResponse> result = new ArrayList<>();

            for (List<AviationApiAirportResponse> airportList : airportsMap.values()) {
                for (AviationApiAirportResponse airport : airportList) {
                    result.add(new AirportResponse(
                            airport.faaIdent,
                            airport.icaoIdent,
                            airport.facilityName,
                            airport.city,
                            airport.state,
                            airport.county,
                            "API"
                    ));
                }
            }

            if (result.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nenhum aeroporto encontrado para os códigos informados");

            return result;

        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Erro ao consultar API externa", e);
        }
    }
}
