package com.caso3.monitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.caso3.monitor.model.EstadoServicio;
import com.caso3.monitor.model.MonitorLog;
import com.caso3.monitor.model.Servicio;

@Repository
public interface MonitorLogRepository extends JpaRepository<MonitorLog, Long>{

    List<MonitorLog> findByServicio(Servicio servicio);

    List<MonitorLog> findTop20ByOrderByFechaRevisionDesc();

    List<MonitorLog> findTop20ByServicioOrderByFechaRevisionDesc(Servicio servicio);

    Optional<MonitorLog> findTopByServicioOrderByFechaRevisionDesc(Servicio servicio);

    List<MonitorLog> findByEstado(EstadoServicio estado);

    long countByEstado(EstadoServicio estado);

}