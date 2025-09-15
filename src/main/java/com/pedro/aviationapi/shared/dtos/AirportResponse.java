package com.pedro.aviationapi.shared.dtos;
public class AirportResponse {
    public String faaCode;
    public String icaoCode;
    public String name;
    public String city;
    public String state;
    public String country;

    public AirportResponse(String faaCode, String icaoCode, String name,
                           String city, String state, String country) {
        this.faaCode = faaCode;
        this.icaoCode = icaoCode;
        this.name = name;
        this.city = city;
        this.state = state;
        this.country = country;
    }
}