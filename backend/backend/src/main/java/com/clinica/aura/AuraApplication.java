package com.clinica.aura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuraApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuraApplication.class, args);
	}

}
