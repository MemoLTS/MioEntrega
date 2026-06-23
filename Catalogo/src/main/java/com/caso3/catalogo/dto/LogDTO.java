package com.caso3.catalogo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {

    private Long id;
    private String servicio;
    private String endpoint;
    private String metodo;
    private Integer estado;
    private Long tiempoRespuesta;
    private String usuario;
    private String ip;
    private String error;
    private LocalDateTime fecha;
}
