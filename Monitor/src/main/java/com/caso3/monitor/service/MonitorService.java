package com.caso3.monitor.service;

import java.util.List;

import com.caso3.monitor.dto.EstadoServicioDTO;
import com.caso3.monitor.dto.EstadisticasDTO;
import com.caso3.monitor.dto.HistorialDTO;
import com.caso3.monitor.dto.RegistrarServicioDTO;
import com.caso3.monitor.model.Servicio;

public interface MonitorService {

    Servicio registrarServicio(RegistrarServicioDTO dto);

    List<EstadoServicioDTO> obtenerEstadoServicios();

    EstadoServicioDTO obtenerEstadoServicio(String nombre);

    List<HistorialDTO> obtenerHistorial();

    List<HistorialDTO> obtenerHistorialServicio(String nombre);

    EstadisticasDTO obtenerEstadisticas();

    void revisarServicios();

}