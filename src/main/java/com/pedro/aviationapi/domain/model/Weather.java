package com.pedro.aviationapi.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Weather {
    private String temperature;
    private String wind;
    private String visibility;
    private Boolean success;
    private String source;
}
