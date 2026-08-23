package com.azoth.somniazodiaca.config;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.context.annotation.Configuration;

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
public class AdminInitializer {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder encoder;
    private final SegnoZodiacaleRepository segnoRepository;
    private final TemaNataleRepository temaNataleRepository;

    public AdminInitializer(
            UtenteRepository utenteRepository,
            PasswordEncoder encoder,
            SegnoZodiacaleRepository segnoRepository,
            TemaNataleRepository temaNataleRepository) {
        this.utenteRepository = utenteRepository;
        this.encoder = encoder;
        this.segnoRepository = segnoRepository;
        this.temaNataleRepository = temaNataleRepository;
    }

    public void inizializza() {
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
                .orElseThrow(() -> new IllegalStateException(
                        "Il segno LEONE non è stato inizializzato"));

        admin.setEmail("admin@somniazodiaca.it");
        admin.setRuolo(Ruolo.ADMIN);
        admin.setQi(10000);
        admin.setGiorniConsecutivi(7);
        admin.setProfiloColore("#F97316");
        admin.setSegnoZodiacale(segno);
        admin.setAscendente(segno);
        utenteRepository.save(admin);

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
    }
}
