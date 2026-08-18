package com.azoth.somniazodiaca.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner initAdmin(UtenteRepository repo, PasswordEncoder encoder) {
        return args -> {

            if (repo.findByUsername("admin").isEmpty()) {

                Utente admin = Utente.builder()
                        .username("admin")
                        .email("admin@somniazodiaca.it")
                        .passwordHash(encoder.encode("admin"))
                        .ruolo(Ruolo.ADMIN)
                        .build();

                repo.save(admin);
                System.out.println("Superutente ADMIN creato automaticamente.");
            }
        };
    }
}
