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

}
