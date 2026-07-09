package com.caso3.monitor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "servicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Integer puerto;

    @Column(nullable = false)
    private String endpointHealth;

    @Column(nullable = false)
    private Boolean activo;
}