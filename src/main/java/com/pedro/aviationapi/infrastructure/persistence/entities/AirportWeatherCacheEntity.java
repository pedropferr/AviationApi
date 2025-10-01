package com.pedro.aviationapi.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weathers_cache",
        indexes = {
                @Index(name = "station_id", columnList = "stationId")
        })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AirportWeatherCacheEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // dados retornados pela API externa
    @Column(nullable = false, unique = true, length = 10)
    private String stationId;
    private String temperature;
    private String wind;
    private String visibility;

    // controle de cache
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
