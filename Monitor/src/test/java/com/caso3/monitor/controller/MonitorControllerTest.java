package com.caso3.monitor.controller;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;

import com.caso3.monitor.dto.*;
import com.caso3.monitor.model.EstadoServicio;
import com.caso3.monitor.model.Servicio;
import com.caso3.monitor.service.MonitorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;



@WebMvcTest(MonitorController.class)
class MonitorControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @SuppressWarnings("removal")
    @MockBean
    private MonitorService monitorService;


    @Autowired
    private ObjectMapper objectMapper;



    private Servicio servicio;
    private EstadoServicioDTO estado;



    @BeforeEach
    void setup(){
        servicio = Servicio.builder()
                .id(1L)
                .nombre("Inventario")
                .url("http://localhost")
                .puerto(8090)
                .endpointHealth("/actuator/health")
                .activo(true)
                .build();
        estado = EstadoServicioDTO.builder()
                .id(1L)
                .nombre("Inventario")
                .estado(EstadoServicio.UP)
                .codigoHttp(200)
                .tiempoRespuesta(100L)
                .mensaje("Servicio disponible")
                .build();
    }

    @Test
    void testRegistrarServicio() throws Exception {
        RegistrarServicioDTO dto =
                RegistrarServicioDTO.builder()
                .nombre("Inventario")
                .url("http://localhost")
                .puerto(8090)
                .endpointHealth("/actuator/health")
                .build();
        when(monitorService.registrarServicio(any()))
                .thenReturn(servicio);
        mockMvc.perform(
                post("/api/v1/monitor/servicio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(dto)
                )
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre")
                .value("Inventario"));
        verify(monitorService)
                .registrarServicio(any());
    }


    @Test
    void testObtenerEstados() throws Exception {
        when(monitorService.obtenerEstadoServicios())
                .thenReturn(List.of(estado));
        mockMvc.perform(
                get("/api/v1/monitor")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nombre")
                .value("Inventario"));
        verify(monitorService)
                .obtenerEstadoServicios();
    }





    @Test
    void testObtenerEstadoServicio() throws Exception {
        when(monitorService.obtenerEstadoServicio("Inventario"))
                .thenReturn(estado);
        mockMvc.perform(
                get("/api/v1/monitor/Inventario")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre")
                .value("Inventario"));



        verify(monitorService)
                .obtenerEstadoServicio("Inventario");

    }





    @Test
    void testObtenerHistorial() throws Exception {


        HistorialDTO historial =
                HistorialDTO.builder()
                .servicio("Inventario")
                .estado(EstadoServicio.UP)
                .codigoHttp(200)
                .mensaje("OK")
                .build();



        when(monitorService.obtenerHistorial())
                .thenReturn(List.of(historial));



        mockMvc.perform(
                get("/api/v1/monitor/historial")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].servicio")
                .value("Inventario"));

    }





    @Test
    void testObtenerHistorialServicio() throws Exception {



        HistorialDTO historial =
                HistorialDTO.builder()
                .servicio("Inventario")
                .estado(EstadoServicio.UP)
                .build();



        when(monitorService.obtenerHistorialServicio("Inventario"))
                .thenReturn(List.of(historial));



        mockMvc.perform(
                get("/api/v1/monitor/historial/Inventario")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].servicio")
                .value("Inventario"));

    }





    @Test
    void testObtenerEstadisticas() throws Exception {
        EstadisticasDTO estadisticas =
                EstadisticasDTO.builder()
                .totalServicios(5)
                .serviciosUp(4)
                .serviciosDown(1)
                .promedioTiempoRespuesta(120.0)
                .totalRevisiones(10L)
                .build();
        when(monitorService.obtenerEstadisticas())
                .thenReturn(estadisticas);
        mockMvc.perform(
                get("/api/v1/monitor/estadisticas")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalServicios")
                .value(5));
    }

    @Test
    void testEjecutarMonitoreo() throws Exception {
        doNothing()
                .when(monitorService)
                .revisarServicios();
        mockMvc.perform(
                post("/api/v1/monitor/ejecutar")
        )
        .andExpect(status().isOk())
        .andExpect(
                content()
                .string(
                "Monitoreo ejecutado correctamente"
                )
        );
        verify(monitorService)
                .revisarServicios();
    }
}