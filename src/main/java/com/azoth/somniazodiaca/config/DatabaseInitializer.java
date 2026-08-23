package com.azoth.somniazodiaca.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner inizializzaDatabase(
            ZodiacoInitializer zodiacoInitializer,
            AdminInitializer adminInitializer,
            BadgeInitializer badgeInitializer) {

        return args -> {
            zodiacoInitializer.inizializza();
            badgeInitializer.inizializza();
            adminInitializer.inizializza();

            System.out.println("Inizializzazione database completata.");
        };
    }
}