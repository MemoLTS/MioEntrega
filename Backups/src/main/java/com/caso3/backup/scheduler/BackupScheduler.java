package com.caso3.backup.scheduler;


import com.caso3.backup.model.BackupConfig;
import com.caso3.backup.repository.BackupConfigRepository;
import com.caso3.backup.service.BackupService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
public class BackupScheduler {

    @Autowired
    private BackupService service;

    @Autowired
    private BackupConfigRepository config;

    @Scheduled(cron = "0 0 2 */2 * *")
    public void backupCadaDosDias(){
        BackupConfig conf =
                config.findById(1L).orElse(null);
        if(conf != null && conf.isActivo()){
            service.crearBackup(
                    "AUTOMATICO"
            );
        }
    }

    @Scheduled(cron = "0 0 3 1 * *")
    public void backupMensual(){
        BackupConfig conf =
                config.findById(1L).orElse(null);
        if(conf != null && conf.isActivo()){
            service.crearBackup(
                    "MENSUAL"
            );
        }
    }
}