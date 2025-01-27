package com.honestefforts.fixengine.service;

import java.time.Clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.validation.ClockProvider;

@ConfigurationPropertiesScan
@EnableAutoConfiguration
@SpringBootApplication
public class Application {
	
	@Bean
	public OpenAPI api() {
		return new OpenAPI()
				.info(new Info().title("Fix It Engine Service")
						.description("Fix It Messages Engine")
						.license(new License().name("Apache 2.0")
								.url("http://springdoc.org")))
				.externalDocs(new ExternalDocumentation()
						.description("Fix It Engine Microsite Documentation")
						.url("https://github.com/gasparin93/fix-engine-service"));
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ClockProvider clockProvider() {
		return Clock::systemDefaultZone;
	}
}
