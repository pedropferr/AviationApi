package com.pedro.aviationapi.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airports_cache",
        indexes = {
                @Index(name = "idx_faa_code", columnList = "faaCode"),
                @Index(name = "idx_icao_code", columnList = "icaoCode")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirportCacheEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // dados retornados pela API externa
    @Column(nullable = false, unique = true, length = 10)
    private String faaCode;
    @Column(nullable = false, unique = true, length = 10)
    private String icaoCode;
    private String name;
    private String city;
    private String state;
    private String country;

    // controle de cache
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaneEntity> planes = new ArrayList<>();
}
