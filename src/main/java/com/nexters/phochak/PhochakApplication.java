package com.nexters.phochak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class PhochakApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhochakApplication.class, args);
	}

}
