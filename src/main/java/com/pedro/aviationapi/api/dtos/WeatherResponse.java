package com.pedro.aviationapi.api.dtos;
public class WeatherResponse {
    public String temperature;
    public String wind;
    public String visibility;
    public Boolean success;
    public String source;

    public WeatherResponse(String temperature, String wind, String visibility, String source) {
        this.temperature = temperature;
        this.wind = wind;
        this.visibility = visibility;
        this.source = source;
    }

    public WeatherResponse() {}
}