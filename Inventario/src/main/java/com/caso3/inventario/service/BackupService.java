package com.caso3.inventario.service;

import com.caso3.inventario.model.BackupLogs;
import com.caso3.inventario.repository.BackupLogRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BackupService {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${backup.path}")
    private String backupPath;

    @Value("${backup.mysqldump}")
    private String mysqlDumpPath;

    @Autowired
    private BackupLogRepository backupLogRepository;

    @PostConstruct
    public void backupInicial() {
        realizarBackup();
    }

    public Process startProcess(ProcessBuilder pb) throws IOException {
        return pb.start();
    }

    @Scheduled(cron = "0 0 2 */2 * *")
    public void realizarBackup() {
        String dbName = extractDbName(datasourceUrl);
        String fullPath = "";
        try {
            File dir = new File(backupPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fecha = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                    .format(new Date());
            String fileName = dbName + "_" + fecha + ".sql";
            fullPath = backupPath + fileName;
            ProcessBuilder pb = new ProcessBuilder(
                    mysqlDumpPath,
                    "-u", dbUser,
                    dbName,
                    "-r", fullPath
            );

            Process process = startProcess(pb);
            int exitCode = process.waitFor();
            BackupLogs log = new BackupLogs();
            log.setDatabaseName(dbName);
            log.setFilePath(fullPath);
            log.setCreatedAt(new Date());
            log.setStatus(exitCode == 0 ? "OK" : "ERROR");
            backupLogRepository.save(log);
        } catch (Exception e) {
            BackupLogs log = new BackupLogs();
            log.setDatabaseName(dbName);
            log.setFilePath(fullPath);
            log.setCreatedAt(new Date());
            log.setStatus("ERROR");
            backupLogRepository.save(log);
            e.printStackTrace();
        }
    }

    private String extractDbName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}