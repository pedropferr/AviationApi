package com.pedro.aviationapi.api.dtos;
public class AirportResponse {
    public String faaCode;
    public String icaoCode;
    public String name;
    public String city;
    public String state;
    public String country;
    public String source;
    public Boolean success;
    public WeatherResponse weather;

    public AirportResponse(String faaCode, String icaoCode, String name,
                           String city, String state, String country, String source) {
        this.faaCode = faaCode;
        this.icaoCode = icaoCode;
        this.name = name;
        this.city = city;
        this.state = state;
        this.country = country;
        this.source = source;
    }

    public AirportResponse() {}
}