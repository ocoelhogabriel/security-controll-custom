package com.ocoelhogabriel.security_control_custom.infrastructure.config;

import com.ocoelhogabriel.security_control_custom.application.service.RecursoServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceInitializer {

    @Bean
    public CommandLineRunner initResources(RecursoServiceImpl recursoService) {
        return args -> {
            System.out.println("Iniciando a verificação e criação de recursos padrão...");
            recursoService.createDefaultResources();
            System.out.println("Verificação e criação de recursos padrão concluída.");
        };
    }
}
