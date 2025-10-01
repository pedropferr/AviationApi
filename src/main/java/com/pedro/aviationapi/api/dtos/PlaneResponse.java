package com.pedro.aviationapi.api.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlaneResponse {
    private String registration;
    private String model;
    private String manufacturer;
    private int yearManufacture;
}