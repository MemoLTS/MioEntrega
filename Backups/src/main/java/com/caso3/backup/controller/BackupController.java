package com.caso3.backup.controller;

import com.caso3.backup.model.Backup;
import com.caso3.backup.model.BackupConfig;

import com.caso3.backup.service.BackupService;
import com.caso3.backup.service.BackupConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backups")
public class BackupController {



    @Autowired
    private BackupService backupService;



    @Autowired
    private BackupConfigService configService;

    @PostMapping
    public Backup crearBackup(){
        return backupService
                .crearBackup("MANUAL");

    }

    @GetMapping
    public List<Backup> listar(){
        return backupService.listar();
    }

    @GetMapping("/ultimo")
    public Backup ultimo(){

        return backupService
                .obtenerUltimo();

    }

    @PutMapping("/activar")
    public BackupConfig activar(){
        return configService.activar();
    }

    @PutMapping("/desactivar")
    public BackupConfig desactivar(){
        return configService.desactivar();
    }

    @GetMapping("/config")
    public BackupConfig config(){
        return configService.obtener();
    }

}