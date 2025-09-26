package com.pedro.aviationapi.infrastructure.clients.aviationapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AviationApiAirportWeatherResponse {

    @JsonProperty("station_id")
    public String stationId;

    public String raw;

    public String temp;

    public String dewpoint;

    public String wind;

    @JsonProperty("wind_vel")
    public String windVel;

    public String visibility;

    @JsonProperty("alt_hg")
    public String altHg;

    @JsonProperty("alt_mb")
    public String altMb;

    public String wx;

    @JsonProperty("auto_report")
    public String autoReport;

    @JsonProperty("sky_conditions")
    public List<skyConditions> skyConditions;

    @JsonProperty("category")
    public String category;

    @JsonProperty("report_type")
    public String reportType;

    @JsonProperty("time_of_obs")
    public String timeOfObs;

    public static class skyConditions {

        public String coverage;

        @JsonProperty("base_agl")
        public String baseAgl;
    }

}
