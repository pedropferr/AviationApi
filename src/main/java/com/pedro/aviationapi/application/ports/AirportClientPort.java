package com.pedro.aviationapi.application.ports;

import com.pedro.aviationapi.domain.model.Airport;
import com.pedro.aviationapi.domain.model.Weather;

public interface AirportClientPort {
    Airport fetchAirport(String code);
    Weather fetchAirportWeather(String code);
}
