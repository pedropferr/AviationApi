package com.pedro.aviationapi.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Airport {
    private String faaCode;
    private String icaoCode;
    private String name;
    private String city;
    private String state;
    private String country;
    private String source;
    private Boolean success;
}
