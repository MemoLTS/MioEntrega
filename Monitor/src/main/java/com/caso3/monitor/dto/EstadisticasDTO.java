package com.caso3.monitor.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticasDTO {

    private Integer totalServicios;

    private Integer serviciosUp;

    private Integer serviciosDown;

    private Double promedioTiempoRespuesta;

    private Long totalRevisiones;

}