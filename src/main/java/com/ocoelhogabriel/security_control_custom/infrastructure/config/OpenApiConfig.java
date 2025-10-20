package com.ocoelhogabriel.security_control_custom.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	private static final Logger logger = LoggerFactory.getLogger(OpenApiConfig.class);

	@Bean
	GroupedOpenApi api() {
		return GroupedOpenApi.builder().group("silo-api").pathsToMatch("/api/**").build();
	}

	@Bean
	OpenAPI myOpenAPI(BuildProperties env) {
		String version = env.getVersion();
		String name = env.getName();
		logger.info("Info OpenApi Config, Name Package: " + env.getArtifact() + ", version - " + version + " - Name: " + name);

		return new OpenAPI().info(new Info().title(name).version(version).description("Silo API backend."));
	}
}