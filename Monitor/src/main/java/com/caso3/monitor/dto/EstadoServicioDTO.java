package com.caso3.monitor.dto;

import java.time.LocalDateTime;

import com.caso3.monitor.model.EstadoServicio;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoServicioDTO {

    private Long id;

    private String nombre;

    private EstadoServicio estado;

    private Integer codigoHttp;

    private Long tiempoRespuesta;

    private String mensaje;

    private LocalDateTime ultimaRevision;

}