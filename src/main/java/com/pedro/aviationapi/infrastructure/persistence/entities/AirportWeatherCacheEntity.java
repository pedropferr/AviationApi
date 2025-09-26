package com.pedro.aviationapi.infrastructure.persistence.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weathers_cache",
        indexes = {
                @Index(name = "station_id", columnList = "stationId")
        })
public class AirportWeatherCacheEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    // dados retornados pela API externa
    @Column(nullable = false, unique = true, length = 10)
    public String stationId;
    public String temperature;
    public String wind;
    public String visibility;

    // controle de cache
    @Column(nullable = false)
    public LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = false)
    public LocalDateTime expiresAt;

    protected AirportWeatherCacheEntity() {}

    public AirportWeatherCacheEntity(String stationId, String temperature, String wind,
                                     String visibility, LocalDateTime expiresAt) {

        this.stationId = stationId;
        this.temperature = temperature;
        this.wind = wind;
        this.visibility = visibility;
        this.expiresAt = expiresAt;
    }
}
