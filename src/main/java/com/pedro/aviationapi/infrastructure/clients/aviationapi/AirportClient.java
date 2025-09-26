package com.pedro.aviationapi.infrastructure.clients.aviationapi;

import com.pedro.aviationapi.api.dtos.WeatherResponse;
import com.pedro.aviationapi.application.ports.AirportClientPort;
import com.pedro.aviationapi.api.dtos.AirportResponse;
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
    public AirportResponse fetchAirport(String code) {
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

    public WeatherResponse fetchAirportWeather(String code) {
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

    private AirportResponse buildAirportResponse(String code, AviationApiAirportResponse airportInfo, Boolean success) {

        AirportResponse response = new AirportResponse();

        response.faaCode = (airportInfo != null && airportInfo.faaIdent != null) ? airportInfo.faaIdent : code;
        response.icaoCode = (airportInfo != null) ? airportInfo.icaoIdent : null;
        response.name = (airportInfo != null && airportInfo.facilityName != null) ? airportInfo.facilityName : "Não encontrado";
        response.city = (airportInfo != null) ? airportInfo.city : null;
        response.state = (airportInfo != null) ? airportInfo.state : null;
        response.country = (airportInfo != null) ? airportInfo.county : null;
        response.source = "API";
        response.success = success;

        return response;
    }

    private WeatherResponse buildWeatherResponse(AviationApiAirportWeatherResponse weatherInfo, Boolean success) {

        WeatherResponse weather = new WeatherResponse();
        weather.temperature = (weatherInfo != null && weatherInfo.temp != null) ? weatherInfo.temp : "Não encontrado";
        weather.wind = (weatherInfo != null) ? weatherInfo.wind : null;
        weather.visibility = (weatherInfo != null) ? weatherInfo.visibility : null;
        weather.success = success;
        weather.source = "API";

        return weather;
    }


}
