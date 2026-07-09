package com.caso3.backup.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caso3.backup.model.Backup;
import com.caso3.backup.repository.BackupRepository;


@Service
public class BackupService {


    @Autowired
    private BackupRepository repository;

    public Backup obtenerUltimo(){
        return repository
                .findTopByOrderByFechaCreacionDesc();
    }

    public Backup crearBackup(String tipo){
        Backup backup = new Backup();
        backup.setNombreArchivo(
            "backup_" + LocalDateTime.now()+".sql"
        );
        backup.setFechaCreacion(
            LocalDateTime.now()
        );
        backup.setTipo(tipo);
        return repository.save(backup);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void validarBackupInicial(){

        if(repository.count() == 0){

            crearBackup("INICIAL");

        }
    }
    public boolean existeBackup(){
        return repository.count() > 0;
    }

    public List<Backup> listar(){
        return repository.findAll();
    }

}