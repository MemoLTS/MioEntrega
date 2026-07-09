package com.caso3.monitor.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.caso3.monitor.dto.*;
import com.caso3.monitor.model.*;
import com.caso3.monitor.repository.*;

@Service
public class MonitorServiceImpl implements MonitorService {

        @Autowired
        private ServicioRepository servicioRepository;

        @Autowired
        private MonitorLogRepository monitorLogRepository;

        @Autowired
        private RestTemplate restTemplate;

        @Override
        public Servicio registrarServicio(RegistrarServicioDTO dto) {

        Servicio servicio = Servicio.builder()
                .nombre(dto.getNombre())
                .url(dto.getUrl())
                .puerto(dto.getPuerto())
                .endpointHealth(dto.getEndpointHealth())
                .activo(true)
                .build();
        return servicioRepository.save(servicio);
    }

    @Override
    public List<HistorialDTO> obtenerHistorial() {
        List<HistorialDTO> lista = new ArrayList<>();
        List<MonitorLog> logs = monitorLogRepository
                .findTop20ByOrderByFechaRevisionDesc();
        for (MonitorLog log : logs) {
            lista.add(HistorialDTO.builder()
                    .servicio(log.getServicio().getNombre())
                    .estado(log.getEstado())
                    .codigoHttp(log.getCodigoHttp())
                    .tiempoRespuesta(log.getTiempoRespuesta())
                    .mensaje(log.getMensaje())
                    .fecha(log.getFechaRevision())
                    .build());
        }
        return lista;
    }

    @Override
    public List<EstadoServicioDTO> obtenerEstadoServicios() {
        List<EstadoServicioDTO> lista = new ArrayList<>();
        List<Servicio> servicios = servicioRepository.findAll();
        for (Servicio servicio : servicios) {
            Optional<MonitorLog> ultimo = monitorLogRepository
                    .findTopByServicioOrderByFechaRevisionDesc(servicio);
            if (ultimo.isPresent()) {
                MonitorLog log = ultimo.get();
                EstadoServicioDTO dto = EstadoServicioDTO.builder()
                        .id(servicio.getId())
                        .nombre(servicio.getNombre())
                        .estado(log.getEstado())
                        .codigoHttp(log.getCodigoHttp())
                        .tiempoRespuesta(log.getTiempoRespuesta())
                        .mensaje(log.getMensaje())
                        .ultimaRevision(log.getFechaRevision())
                        .build();
                lista.add(dto);
            }
        }
        return lista;
    }

    @Override
    public List<HistorialDTO> obtenerHistorialServicio(String nombre) {
        Servicio servicio = servicioRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        List<HistorialDTO> lista = new ArrayList<>();
        List<MonitorLog> logs = monitorLogRepository
                .findTop20ByServicioOrderByFechaRevisionDesc(servicio);
        for (MonitorLog log : logs) {
            lista.add(HistorialDTO.builder()
                    .servicio(servicio.getNombre())
                    .estado(log.getEstado())
                    .codigoHttp(log.getCodigoHttp())
                    .tiempoRespuesta(log.getTiempoRespuesta())
                    .mensaje(log.getMensaje())
                    .fecha(log.getFechaRevision())
                    .build());
        }
        return lista;
    }

    @Override
    public EstadisticasDTO obtenerEstadisticas() {
        long total = servicioRepository.count();
        long up = monitorLogRepository.countByEstado(EstadoServicio.UP);
        long down = monitorLogRepository.countByEstado(EstadoServicio.DOWN);
        List<MonitorLog> logs = monitorLogRepository.findAll();
        double promedio = logs.stream()
                .mapToLong(MonitorLog::getTiempoRespuesta)
                .average()
                .orElse(0);
        return EstadisticasDTO.builder()
                .totalServicios((int) total)
                .serviciosUp((int) up)
                .serviciosDown((int) down)
                .promedioTiempoRespuesta(promedio)
                .totalRevisiones((long) logs.size())
                .build();
    }

    @Override
    public EstadoServicioDTO obtenerEstadoServicio(String nombre) {
        Servicio servicio = servicioRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        MonitorLog log = monitorLogRepository
                .findTopByServicioOrderByFechaRevisionDesc(servicio)
                .orElseThrow(() -> new RuntimeException("No existen registros"));
        return EstadoServicioDTO.builder()
                .id(servicio.getId())
                .nombre(servicio.getNombre())
                .estado(log.getEstado())
                .codigoHttp(log.getCodigoHttp())
                .tiempoRespuesta(log.getTiempoRespuesta())
                .mensaje(log.getMensaje())
                .ultimaRevision(log.getFechaRevision())
                .build();
    }

    @Override
    public void revisarServicios() {
        List<Servicio> servicios = servicioRepository.findAll();
        for (Servicio servicio : servicios) {
            MonitorLog log = new MonitorLog();
            long inicio = System.currentTimeMillis();
            try {
                String url = servicio.getUrl() + ":" +
                            servicio.getPuerto() +
                            servicio.getEndpointHealth();
                ResponseEntity<String> respuesta =
                        restTemplate.getForEntity(url, String.class);
                log.setServicio(servicio);
                log.setEstado(EstadoServicio.UP);
                log.setCodigoHttp(respuesta.getStatusCode().value());
                log.setMensaje("Servicio disponible");
            } catch (Exception e) {
                log.setServicio(servicio);
                log.setEstado(EstadoServicio.DOWN);
                log.setCodigoHttp(500);
                log.setMensaje(e.getMessage());
                }
                long fin = System.currentTimeMillis();
                log.setTiempoRespuesta(fin - inicio);
                log.setFechaRevision(LocalDateTime.now());
                monitorLogRepository.save(log);
            }
        }
}