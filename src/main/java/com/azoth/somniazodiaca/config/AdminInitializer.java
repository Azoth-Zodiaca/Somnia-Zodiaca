package com.azoth.somniazodiaca.config;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.azoth.somniazodiaca.entities.SegnoZodiacale;
import com.azoth.somniazodiaca.entities.TemaNatale;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.enums.SegnoZodiacaleEnum;
import com.azoth.somniazodiaca.repositories.SegnoZodiacaleRepository;
import com.azoth.somniazodiaca.repositories.TemaNataleRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(2)
public class AdminInitializer {

    @Bean
    public CommandLineRunner initAdmin(
            UtenteRepository utenteRepository,
            PasswordEncoder encoder,
            SegnoZodiacaleRepository segnoRepository,
            TemaNataleRepository temaNataleRepository) {

        return args -> {
            Utente admin = utenteRepository
                    .findByUsername("admin")
                    .orElseGet(() -> {
                        Utente nuovoAdmin = Utente.builder()
                                .username("admin")
                                .email("admin@somniazodiaca.it")
                                .passwordHash(encoder.encode("admin"))
                                .ruolo(Ruolo.ADMIN)
                                .qi(10000)
                                .giorniConsecutivi(7)
                                .profiloColore("#F97316")
                                .build();

                        return utenteRepository.save(nuovoAdmin);
                    });

            SegnoZodiacale segno = segnoRepository
                    .findBySegnoZodiacale(SegnoZodiacaleEnum.LEONE)
                    .orElse(null);

            if (segno != null) {
                admin.setSegnoZodiacale(segno);
                admin.setAscendente(segno);
                utenteRepository.save(admin);
            }

            if (temaNataleRepository.findByUtenteId(admin.getId()).isEmpty()) {
                TemaNatale temaNatale = TemaNatale.builder()
                        .utente(admin)
                        .dataNascita(LocalDate.of(1995, 8, 10))
                        .oraNascita(LocalTime.of(4, 35))
                        .luogoNascita("Roma")
                        .latitudine(new java.math.BigDecimal("41.9028"))
                        .longitudine(new java.math.BigDecimal("12.4964"))
                        .timezone("Europe/Rome")
                        .dataCreazione(java.time.LocalDateTime.now())
                        .build();

                temaNataleRepository.save(temaNatale);
            }

            System.out.println("Profilo ADMIN completo disponibile.");
        };
    }
}
