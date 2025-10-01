package com.pedro.aviationapi.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "planes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlaneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String registration;
    private String model;
    private String manufacturer;
    @Column(length = 4)
    private int yearManufacture;

    // ----------------------------------------------------
    // Mapeamento Many-to-One
    // @JoinColumn cria a coluna 'airport_id' na tabela 'planes'
    // O campo 'airport' é o que foi referenciado no 'mappedBy' em AirportCacheEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airport_id", nullable = false)
    private AirportCacheEntity airport;

}
