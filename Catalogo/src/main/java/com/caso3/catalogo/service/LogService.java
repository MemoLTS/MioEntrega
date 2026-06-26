package com.caso3.catalogo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caso3.catalogo.dto.LogDTO;
import com.caso3.catalogo.repository.LogRepository;

@Service
public class LogService {

    @Autowired
    private LogRepository repository;

    public LogDTO guardar(LogDTO log) {

        if (log.getFecha() == null) {
            log.setFecha(LocalDateTime.now());
        }

        return repository.save(log);
    }

    public List<LogDTO> listar() {
        return repository.findAll();
    }
}