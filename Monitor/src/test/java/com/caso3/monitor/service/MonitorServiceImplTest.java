package com.caso3.monitor.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.caso3.monitor.dto.*;
import com.caso3.monitor.model.*;
import com.caso3.monitor.repository.*;

@ExtendWith(MockitoExtension.class)
class MonitorServiceImplTest {


        @Mock
        private ServicioRepository servicioRepository;

        @Mock
        private MonitorLogRepository monitorLogRepository;

        @Mock
        private RestTemplate restTemplate;


        @InjectMocks
        private MonitorServiceImpl service;


        private Servicio servicio;
        private MonitorLog log;


        @BeforeEach
        void setup(){
                MockitoAnnotations.openMocks(this);
                servicio = Servicio.builder()
                        .id(1L)
                        .nombre("Inventario")
                        .url("http://localhost")
                        .puerto(8090)
                        .endpointHealth("/actuator/health")
                        .activo(true)
                        .build();
                log = MonitorLog.builder()
                        .id(1L)
                        .servicio(servicio)
                        .estado(EstadoServicio.UP)
                        .codigoHttp(200)
                        .mensaje("OK")
                        .tiempoRespuesta(100L)
                        .fechaRevision(LocalDateTime.now())
                        .build();
        }

        @Test
        void registrarServicio(){
                RegistrarServicioDTO dto =
                        RegistrarServicioDTO.builder()
                        .nombre("Inventario")
                        .url("http://localhost")
                        .puerto(8090)
                        .endpointHealth("/health")
                        .build();
                when(servicioRepository.save(any()))
                        .thenReturn(servicio);
                Servicio resultado =
                        service.registrarServicio(dto);
                assertNotNull(resultado);
                assertEquals("Inventario",
                        resultado.getNombre());
                verify(servicioRepository)
                        .save(any());
        }


        @Test
        void obtenerHistorial(){
                when(monitorLogRepository
                        .findTop20ByOrderByFechaRevisionDesc())
                        .thenReturn(List.of(log));
                List<HistorialDTO> resultado =
                        service.obtenerHistorial();
                assertEquals(1, resultado.size());
                assertEquals(
                        "Inventario",
                        resultado.get(0).getServicio()
                );
        }

        @Test
        void obtenerEstadoServicios(){
                when(servicioRepository.findAll())
                        .thenReturn(List.of(servicio));
                when(monitorLogRepository
                        .findTopByServicioOrderByFechaRevisionDesc(servicio))
                        .thenReturn(Optional.of(log));
                List<EstadoServicioDTO> resultado =
                        service.obtenerEstadoServicios();
                assertEquals(1, resultado.size());
                assertEquals(
                        EstadoServicio.UP,
                        resultado.get(0).getEstado()
                );
        }


        @Test
        void obtenerHistorialServicio(){
                when(servicioRepository.findByNombre("Inventario"))
                        .thenReturn(Optional.of(servicio));
                when(monitorLogRepository
                        .findTop20ByServicioOrderByFechaRevisionDesc(servicio))
                        .thenReturn(List.of(log));
                List<HistorialDTO> resultado =
                        service.obtenerHistorialServicio(
                                "Inventario"
                        );
                assertEquals(1, resultado.size());
        }


        @Test
        void obtenerHistorialServicioNoExiste(){
                when(servicioRepository.findByNombre("NoExiste"))
                        .thenReturn(Optional.empty());
                assertThrows(
                        RuntimeException.class,
                        () -> service
                        .obtenerHistorialServicio("NoExiste")
                );

        }





        @Test
        void obtenerEstadoServicio(){
                when(servicioRepository.findByNombre("Inventario"))
                        .thenReturn(Optional.of(servicio));
                when(monitorLogRepository
                        .findTopByServicioOrderByFechaRevisionDesc(servicio))
                        .thenReturn(Optional.of(log));

                EstadoServicioDTO resultado =
                        service.obtenerEstadoServicio(
                                "Inventario"
                        );
                assertEquals(
                        "Inventario",
                        resultado.getNombre()
                );
        }


        @Test
        void obtenerEstadisticas(){
                when(servicioRepository.count())
                        .thenReturn(3L);
                when(monitorLogRepository
                        .countByEstado(EstadoServicio.UP))
                        .thenReturn(2L);
                when(monitorLogRepository
                        .countByEstado(EstadoServicio.DOWN))
                        .thenReturn(1L);
                when(monitorLogRepository.findAll())
                        .thenReturn(List.of(log));

                EstadisticasDTO resultado =
                        service.obtenerEstadisticas();

                assertEquals(
                        3,
                        resultado.getTotalServicios()
                );
                assertEquals(
                        2,
                        resultado.getServiciosUp()
                );

        }


        @Test
        void revisarServiciosUP(){
                when(servicioRepository.findAll())
                        .thenReturn(List.of(servicio));
                when(restTemplate.getForEntity(
                        anyString(),
                        eq(String.class)
                ))
                .thenReturn(
                        new ResponseEntity<>(
                                "OK",
                                HttpStatus.OK
                        )
                );
                service.revisarServicios();
                verify(monitorLogRepository)
                        .save(any(MonitorLog.class));

        }

        @Test
        void revisarServiciosDOWN(){
                when(servicioRepository.findAll())
                        .thenReturn(List.of(servicio));
                when(restTemplate.getForEntity(
                        anyString(),
                        eq(String.class)
                ))
                .thenThrow(
                        new RuntimeException("No disponible")
                );

                service.revisarServicios();
                verify(monitorLogRepository)
                        .save(any(MonitorLog.class));
        }
}