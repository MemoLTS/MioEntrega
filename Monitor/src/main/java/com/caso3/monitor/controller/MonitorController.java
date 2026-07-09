package com.caso3.monitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.caso3.monitor.dto.*;
import com.caso3.monitor.model.Servicio;
import com.caso3.monitor.service.MonitorService;

@RestController
@RequestMapping("/api/v1/monitor")
@CrossOrigin("*")
public class MonitorController {

        @Autowired
        private MonitorService monitorService;

        @PostMapping("/servicio")
        public ResponseEntity<Servicio> registrarServicio(
                @RequestBody RegistrarServicioDTO dto) {

                return ResponseEntity.ok(
                        monitorService.registrarServicio(dto));
        }

        @GetMapping
        public ResponseEntity<List<EstadoServicioDTO>> obtenerEstados() {

                return ResponseEntity.ok(
                        monitorService.obtenerEstadoServicios());
        }

        @GetMapping("/{nombre}")
        public ResponseEntity<EstadoServicioDTO> obtenerEstado(
                @PathVariable String nombre) {

                return ResponseEntity.ok(
                        monitorService.obtenerEstadoServicio(nombre));
        }

        @GetMapping("/historial")
        public ResponseEntity<List<HistorialDTO>> obtenerHistorial() {

                return ResponseEntity.ok(
                        monitorService.obtenerHistorial());
        }

        @GetMapping("/historial/{nombre}")
        public ResponseEntity<List<HistorialDTO>> obtenerHistorialServicio(
                @PathVariable String nombre) {

                return ResponseEntity.ok(
                        monitorService.obtenerHistorialServicio(nombre));
        }

        @GetMapping("/estadisticas")
        public ResponseEntity<EstadisticasDTO> obtenerEstadisticas() {

                return ResponseEntity.ok(
                        monitorService.obtenerEstadisticas());
        }

        @PostMapping("/ejecutar")
        public ResponseEntity<String> ejecutarMonitoreo() {

                monitorService.revisarServicios();

                return ResponseEntity.ok(
                        "Monitoreo ejecutado correctamente");
        }
}