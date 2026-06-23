package com.caso3.inventario.model;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "backup_logs")
@Data
public class BackupLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String databaseName;
    private String filePath;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}