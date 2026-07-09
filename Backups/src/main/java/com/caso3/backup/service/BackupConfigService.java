package com.caso3.backup.service;


import com.caso3.backup.model.BackupConfig;
import com.caso3.backup.repository.BackupConfigRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class BackupConfigService {

    @Autowired
    private BackupConfigRepository repository;

    public BackupConfig obtener(){
        return repository
                .findById(1L)
                .orElseGet(() -> {
                    BackupConfig config =
                            new BackupConfig();
                    config.setId(1L);
                    config.setActivo(false);
                    return repository.save(config);
                });
    }




    public BackupConfig activar(){
        BackupConfig config = obtener();
        config.setActivo(true);
        return repository.save(config);
    }




    public BackupConfig desactivar(){
        BackupConfig config = obtener();
        config.setActivo(false);
        return repository.save(config);
    }

}