package com.caso3.backup.scheduler;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.caso3.backup.model.BackupConfig;
import com.caso3.backup.repository.BackupConfigRepository;
import com.caso3.backup.service.BackupService;

@ExtendWith(MockitoExtension.class)
class BackupSchedulerTest {


        @Mock
        private BackupService service;


        @Mock
        private BackupConfigRepository config;


        @InjectMocks
        private BackupScheduler scheduler;



        @Test
        void backupCadaDosDiasConConfiguracionActiva() {

                BackupConfig conf = new BackupConfig();
                conf.setId(1L);
                conf.setActivo(true);


                when(config.findById(1L))
                        .thenReturn(Optional.of(conf));


                scheduler.backupCadaDosDias();


                verify(service)
                        .crearBackup("AUTOMATICO");
        }



        @Test
        void backupCadaDosDiasConConfiguracionDesactivada() {

                BackupConfig conf = new BackupConfig();
                conf.setId(1L);
                conf.setActivo(false);


                when(config.findById(1L))
                        .thenReturn(Optional.of(conf));


                scheduler.backupCadaDosDias();


                verify(service, never())
                        .crearBackup("AUTOMATICO");
        }



        @Test
        void backupCadaDosDiasSinConfiguracion() {

                when(config.findById(1L))
                        .thenReturn(Optional.empty());


                scheduler.backupCadaDosDias();


                verify(service, never())
                        .crearBackup("AUTOMATICO");
        }



        @Test
        void backupMensualConConfiguracionActiva() {

                BackupConfig conf = new BackupConfig();
                conf.setId(1L);
                conf.setActivo(true);


                when(config.findById(1L))
                        .thenReturn(Optional.of(conf));


                scheduler.backupMensual();


                verify(service)
                        .crearBackup("MENSUAL");
        }



        @Test
        void backupMensualConConfiguracionDesactivada() {

                BackupConfig conf = new BackupConfig();
                conf.setId(1L);
                conf.setActivo(false);


                when(config.findById(1L))
                        .thenReturn(Optional.of(conf));


                scheduler.backupMensual();


                verify(service, never())
                        .crearBackup("MENSUAL");
        }



        @Test
        void backupMensualSinConfiguracion() {

                when(config.findById(1L))
                        .thenReturn(Optional.empty());


                scheduler.backupMensual();


                verify(service, never())
                        .crearBackup("MENSUAL");
        }

}