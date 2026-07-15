package com.caso3.backup.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Microservicio Backup - EcoMarket",
        version = "1.0",
        description = "API REST para la gestión de respaldos del sistema EcoMarket"
    ),
    servers = @Server(url = "http://localhost:8086", description = "Servidor local")
)
public class OpenApiConfig {

}
