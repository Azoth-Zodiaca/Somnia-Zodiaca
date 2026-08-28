package com.azoth.somniazodiaca.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.entities.SegnoZodiacale;
import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.entities.TemaNatale;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.enums.SegnoZodiacaleEnum;
import com.azoth.somniazodiaca.enums.StileEnum;
import com.azoth.somniazodiaca.enums.UmoreEnum;
import com.azoth.somniazodiaca.repositories.InterpretazioneRepository;
import com.azoth.somniazodiaca.repositories.SegnoZodiacaleRepository;
import com.azoth.somniazodiaca.repositories.SognoRepository;
import com.azoth.somniazodiaca.repositories.TemaNataleRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

@Configuration
public class AdminInitializer {

    private static final String PASSWORD_DEMO = "SomniaDemo2026!";

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder encoder;
    private final SegnoZodiacaleRepository segnoRepository;
    private final TemaNataleRepository temaNataleRepository;
    private final SognoRepository sognoRepository;
    private final InterpretazioneRepository interpretazioneRepository;

    public AdminInitializer(
            UtenteRepository utenteRepository,
            PasswordEncoder encoder,
            SegnoZodiacaleRepository segnoRepository,
            TemaNataleRepository temaNataleRepository,
            SognoRepository sognoRepository,
            InterpretazioneRepository interpretazioneRepository) {
        this.utenteRepository = utenteRepository;
        this.encoder = encoder;
        this.segnoRepository = segnoRepository;
        this.temaNataleRepository = temaNataleRepository;
        this.sognoRepository = sognoRepository;
        this.interpretazioneRepository = interpretazioneRepository;
    }

    public void inizializza() {
        DatiUtente datiAdmin = new DatiUtente(
                "admin", "admin@somniazodiaca.it", Ruolo.ADMIN,
                SegnoZodiacaleEnum.LEONE, SegnoZodiacaleEnum.LEONE,
                LocalDate.of(1995, 8, 10), LocalTime.of(4, 35), "Roma",
                new BigDecimal("41.9028"), new BigDecimal("12.4964"),
                10000, 6);

        Utente admin = inizializzaUtente(datiAdmin);
        inizializzaSogniAdmin(admin);

        System.out.println("Profilo ADMIN completo disponibile.");
    }

    private Utente inizializzaUtente(DatiUtente dati) {
        Utente utente = utenteRepository
                .findByUsername(dati.username())
                .orElseGet(Utente::new);

        utente.setUsername(dati.username());
        utente.setEmail(dati.email());
        utente.setPasswordHash(encoder.encode(PASSWORD_DEMO));
        utente.setRuolo(dati.ruolo());
        utente.setQi(dati.qi());
        utente.setSegnoZodiacale(trovaSegno(dati.segno()));
        utente.setAscendente(trovaSegno(dati.ascendente()));
        utente.setGiorniConsecutivi(dati.giorniConsecutivi());
        utente.setGiorniRicompensaGiornaliera(dati.giorniConsecutivi());
        utente.setUltimaRicompensaGiornaliera(LocalDate.now().minusDays(1));

        Utente adminSalvato = utenteRepository.save(utente);
        inizializzaTemaNatale(adminSalvato, dati);

        return adminSalvato;
    }

    private SegnoZodiacale trovaSegno(SegnoZodiacaleEnum segno) {
        return segnoRepository.findBySegnoZodiacale(segno)
                .orElseThrow(() -> new IllegalStateException("Il segno " + segno + " non è stato inizializzato"));
    }

    private void inizializzaTemaNatale(Utente utente, DatiUtente dati) {
        if (temaNataleRepository.findByUtenteId(utente.getId()).isEmpty()) {
            temaNataleRepository.save(TemaNatale.builder()
                    .utente(utente)
                    .dataNascita(dati.dataNascita())
                    .oraNascita(dati.oraNascita())
                    .luogoNascita(dati.luogoNascita())
                    .latitudine(dati.latitudine())
                    .longitudine(dati.longitudine())
                    .timezone("Europe/Rome")
                    .dataCreazione(LocalDateTime.now())
                    .build());
        }
    }

    private record DatiUtente(String username, String email, Ruolo ruolo, SegnoZodiacaleEnum segno,
            SegnoZodiacaleEnum ascendente, LocalDate dataNascita, LocalTime oraNascita, String luogoNascita,
            BigDecimal latitudine, BigDecimal longitudine, int qi, int giorniConsecutivi) {
    }

    private void inizializzaSogniAdmin(Utente admin) {
        if (!sognoRepository.findByUtenteId(admin.getId()).isEmpty()) {
            return; // già popolati, non duplicare
        }

        creaSognoConInterpretazione(admin,
                "Ho sognato di camminare sotto un cielo pieno di stelle.",
                "Interpretazione junghiana del sogno",
                "Sognare di camminare sotto un manto di stelle è un'esperienza dal profondo valore cosmico...",
                UmoreEnum.SERENO, StileEnum.ASTROLOGICO,
                LocalDateTime.now().plusHours(48));

        creaSognoConInterpretazione(admin,
                "Ho sognato una casa luminosa vicino al mare.",
                "Interpretazione simbolica del sogno",
                "Un sogno meraviglioso, che sembra donarti una piacevole ventata di armonia...",
                UmoreEnum.SERENO, StileEnum.SIMBOLICO, null);

        creaSognoConInterpretazione(admin,
                "Ho sognato un orologio che correva velocissimo.",
                "Interpretazione junghiana del sogno",
                "Accogliere questo sogno significa mettersi in ascolto di un messaggio profondo...",
                UmoreEnum.ANSIOSO, StileEnum.JUNGHIANO,
                LocalDateTime.now().plusMinutes(1));

        creaSognoConInterpretazione(admin,
                "Ho sognato una porta chiusa.",
                "Interpretazione junghiana del sogno",
                "Sognare una porta chiusa è un'esperienza evocativa...",
                UmoreEnum.CONFUSO, StileEnum.SIMBOLICO,
                LocalDateTime.now().minusMinutes(1));
    }

    private void creaSognoConInterpretazione(Utente utente, String testoSogno, String prompt,
            String testoInterpretazione, UmoreEnum umore, StileEnum stile,
            LocalDateTime scadenzaCache) {
        Sogno sogno = sognoRepository.save(Sogno.builder()
                .utente(utente)
                .testo(testoSogno)
                .build());

        interpretazioneRepository.save(Interpretazione.builder()
                .sogno(sogno)
                .prompt(prompt)
                .testo(testoInterpretazione)
                .umore(umore)
                .stile(stile)
                .scadenzaCache(scadenzaCache)
                .build());
    }
}
