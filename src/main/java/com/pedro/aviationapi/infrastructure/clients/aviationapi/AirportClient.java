package com.pedro.aviationapi.infrastructure.clients.aviationapi;

import com.pedro.aviationapi.application.ports.AirportClientPort;
import com.pedro.aviationapi.domain.model.Airport;
import com.pedro.aviationapi.domain.model.Weather;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class AirportClient implements AirportClientPort {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://api.aviationapi.com/";

    /**
     * Busca um aeroporto por código na api <a href="https://aviationapi.com/">...</a>
     *
     * @param code Código IATA ou ICAO do aeroporto
     * @return AirportResponse com os dados do aeroporto
     */
    @Override
    public Airport fetchAirport(String code) {
        try {
            String url = BASE_URL + "v1/airports?apt=" + code.trim();

            ResponseEntity<Map<String, List<AviationApiAirportResponse>>> responseEntity =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
                    );

            Map<String, List<AviationApiAirportResponse>> airportsMap = responseEntity.getBody();

            if(airportsMap == null || airportsMap.isEmpty())
                return buildAirportResponse(code, new AviationApiAirportResponse(), false);

            for (List<AviationApiAirportResponse> airportList : airportsMap.values()) {
                if (!airportList.isEmpty())
                    return buildAirportResponse(code, airportList.get(0), true);
            }

            return buildAirportResponse(code, new AviationApiAirportResponse(), false);
        } catch (Exception e) {
            return buildAirportResponse(code, new AviationApiAirportResponse(), false);
        }
    }

    public Weather fetchAirportWeather(String code) {
        try {
            String url = BASE_URL + "v1/weather/metar?apt=" + code.trim();

            ResponseEntity<Map<String, List<AviationApiAirportWeatherResponse>>> responseEntity =
                    restTemplate.exchange(
                            url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
                    );

            Map<String, List<AviationApiAirportWeatherResponse>> airportsMap = responseEntity.getBody();

            if (airportsMap == null || airportsMap.isEmpty())
                return buildWeatherResponse(new AviationApiAirportWeatherResponse(), false);

            for (List<AviationApiAirportWeatherResponse> airportList : airportsMap.values()) {
                if (!airportList.isEmpty())
                    return buildWeatherResponse(airportList.get(0), true);
            }

            return buildWeatherResponse(new AviationApiAirportWeatherResponse(), false);
        } catch (Exception e) {
            return buildWeatherResponse(new AviationApiAirportWeatherResponse(), false);
        }
    }

    private Airport buildAirportResponse(String code, AviationApiAirportResponse airportInfo, Boolean success) {

        return Airport.builder()
                .faaCode((airportInfo != null && airportInfo.faaIdent != null) ? airportInfo.faaIdent : code)
                .icaoCode((airportInfo != null) ? airportInfo.icaoIdent : null)
                .name((airportInfo != null && airportInfo.facilityName != null) ? airportInfo.facilityName : "Não encontrado")
                .state((airportInfo != null) ? airportInfo.city : null)
                .country((airportInfo != null) ? airportInfo.county : null)
                .city((airportInfo != null) ? airportInfo.city : null)
                .source("API")
                .success(success)
                .build();
    }

    private Weather buildWeatherResponse(AviationApiAirportWeatherResponse weatherInfo, Boolean success) {

        return Weather.builder()
                .temperature((weatherInfo != null && weatherInfo.temp != null) ? weatherInfo.temp : "Não encontrado")
                .wind((weatherInfo != null) ? weatherInfo.wind : null)
                .visibility((weatherInfo != null) ? weatherInfo.visibility : null)
                .success(success)
                .source("API")
                .build();
    }


}
