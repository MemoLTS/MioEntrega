package com.caso3.monitor.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.caso3.monitor.model.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long>{

    Optional<Servicio> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

}