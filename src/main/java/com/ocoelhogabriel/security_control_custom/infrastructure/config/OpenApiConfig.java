package com.ocoelhogabriel.security_control_custom.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class OpenApiConfig {
	private static final Logger logger = LoggerFactory.getLogger(OpenApiConfig.class);

	@Bean
	GroupedOpenApi api() {
		return GroupedOpenApi.builder().group("silo-api").pathsToMatch("/api/**").build();
	}

	@Bean
	OpenAPI myOpenAPI(Optional<BuildProperties> buildProperties) {
		String version = "1.0.0"; // Default version
		String name = "security-controll-custom"; // Default name

		if (buildProperties.isPresent()) {
			BuildProperties properties = buildProperties.get();
			version = properties.getVersion();
			name = properties.getName();
			logger.info("OpenAPI Config using BuildProperties: Name={}, Version={}", name, version);
		} else {
			logger.warn("BuildProperties not found. Using default OpenAPI info. Name={}, Version={}", name, version);
		}

		return new OpenAPI().info(new Info().title(name).version(version).description("Silo API backend."));
	}
}