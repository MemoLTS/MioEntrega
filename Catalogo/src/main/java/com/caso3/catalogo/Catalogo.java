package com.caso3.catalogo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Catalogo {

	public static void main(String[] args) {
		SpringApplication.run(Catalogo.class, args);
	}

}
