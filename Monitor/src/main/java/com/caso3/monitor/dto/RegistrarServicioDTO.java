package com.caso3.monitor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrarServicioDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String url;

    @NotNull
    private Integer puerto;

    @NotBlank
    private String endpointHealth;

}