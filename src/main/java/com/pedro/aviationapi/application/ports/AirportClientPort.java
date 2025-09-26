package com.pedro.aviationapi.application.ports;

import com.pedro.aviationapi.api.dtos.AirportResponse;
import com.pedro.aviationapi.api.dtos.WeatherResponse;

public interface AirportClientPort {
    AirportResponse fetchAirport(String code);
    WeatherResponse fetchAirportWeather(String code);
}
