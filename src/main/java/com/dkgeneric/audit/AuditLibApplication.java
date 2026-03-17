package com.dkgeneric.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.dkgeneric.*" })
public class AuditLibApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditLibApplication.class, args);
	}

}
