package com.pedro.aviationapi.infrastructure.persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "airports_cache",
        indexes = {
                @Index(name = "idx_faa_code", columnList = "faaCode"),
                @Index(name = "idx_icao_code", columnList = "icaoCode")
        })
public class AirportCacheEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    // dados retornados pela API externa
    @Column(nullable = false, unique = true, length = 10)
    public String faaCode;
    @Column(nullable = false, unique = true, length = 10)
    public String icaoCode;
    public String name;
    public String city;
    public String state;
    public String country;

    // controle de cache
    @Column(nullable = false)
    public LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = false)
    public LocalDateTime expiresAt;

    protected AirportCacheEntity() {}

    public AirportCacheEntity(String faaCode, String icaoCode, String name,
                              String city, String state, String country, LocalDateTime expiresAt) {
        this.faaCode = faaCode;
        this.icaoCode = icaoCode;
        this.name = name;
        this.city = city;
        this.state = state;
        this.country = country;
        this.expiresAt = expiresAt;
    }
}
