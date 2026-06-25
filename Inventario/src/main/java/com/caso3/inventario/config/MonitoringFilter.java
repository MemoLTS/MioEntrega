package com.caso3.inventario.config;

import com.caso3.inventario.dto.LogDTO;
import com.caso3.inventario.service.LogService;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class MonitoringFilter implements Filter {

    private final LogService logService;

    public MonitoringFilter(LogService logService) {
        this.logService = logService;
    }
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        long inicio = System.currentTimeMillis();
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        try {
            chain.doFilter(request, response);
        } finally {
            String metodo = req.getMethod();
            if (!metodo.equals("POST")
                    && !metodo.equals("PUT")
                    && !metodo.equals("DELETE")) {
                return;
            }
            long fin = System.currentTimeMillis();
            LogDTO log = new LogDTO();
            log.setServicio("productos-api");
            log.setEndpoint(req.getRequestURI());
            log.setMetodo(metodo);
            log.setEstado(res.getStatus());
            log.setTiempoRespuesta(fin - inicio);
            log.setIp(req.getRemoteAddr());
            log.setFecha(LocalDateTime.now());
            log.setUsuario("Sistema");
            if (res.getStatus() >= 400) {
                log.setError("HTTP " + res.getStatus());
            }
            try {
                logService.guardar(log);
            } catch (Exception e) {
                System.out.println("No se pudo guardar el log: " + e.getMessage());
            }
        }
    }
}
