package com.caso3.backup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BackupApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                BackupApplication.class,args);
    }

}