package com.azoth.somniazodiaca.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Configuration;

import com.azoth.somniazodiaca.entities.SegnoZodiacale;
import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.entities.Interpretazione;
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

import org.springframework.security.crypto.password.PasswordEncoder;

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
        Utente admin = inizializzaUtente(new DatiUtente("admin", "admin@somniazodiaca.it", Ruolo.ADMIN,
            SegnoZodiacaleEnum.LEONE, SegnoZodiacaleEnum.LEONE, LocalDate.of(1995, 8, 10),
            LocalTime.of(4, 35), "Roma", new BigDecimal("41.9028"), new BigDecimal("12.4964"), 10000, 6));

        inizializzaContenuti(admin, List.of(
            new DatiSogno("Ho sognato di camminare sotto un cielo pieno di stelle.",
                "Il cielo stellato richiama il desiderio di esplorare nuove possibilita interiori.",
                UmoreEnum.SERENO, StileEnum.ASTROLOGICO),
            new DatiSogno("Ho sognato una casa luminosa vicino al mare.",
                "La casa rappresenta la tua identita, mentre il mare richiama la dimensione emotiva.",
                UmoreEnum.SERENO, StileEnum.SIMBOLICO)));

        List<DatiUtente> utentiDemo = List.of(
            new DatiUtente("sofia_rossi", "sofia.rossi@demo.it", Ruolo.BASE, SegnoZodiacaleEnum.PESCI,
                SegnoZodiacaleEnum.ACQUARIO, LocalDate.of(1998, 3, 12), LocalTime.of(7, 20), "Milano",
                new BigDecimal("45.4642"), new BigDecimal("9.1900"), 500, 3),
            new DatiUtente("marco_bianchi", "marco.bianchi@demo.it", Ruolo.BASE, SegnoZodiacaleEnum.ARIETE,
                SegnoZodiacaleEnum.PESCI, LocalDate.of(1992, 4, 2), LocalTime.of(18, 10), "Torino",
                new BigDecimal("45.0703"), new BigDecimal("7.6869"), 350, 5),
            new DatiUtente("elisa_romano", "elisa.romano@demo.it", Ruolo.BASE, SegnoZodiacaleEnum.ACQUARIO,
                SegnoZodiacaleEnum.ARIETE, LocalDate.of(1997, 2, 8), LocalTime.of(12, 45), "Bologna",
                new BigDecimal("44.4949"), new BigDecimal("11.3426"), 720, 8),
            new DatiUtente("luca_ferrari", "luca.ferrari@demo.it", Ruolo.BASE, SegnoZodiacaleEnum.LEONE,
                SegnoZodiacaleEnum.ARIETE, LocalDate.of(1990, 8, 1), LocalTime.of(9, 5), "Napoli",
                new BigDecimal("40.8518"), new BigDecimal("14.2681"), 640, 6),
            new DatiUtente("giulia_conti", "giulia.conti@demo.it", Ruolo.PREMIUM, SegnoZodiacaleEnum.LEONE,
                SegnoZodiacaleEnum.ACQUARIO, LocalDate.of(1996, 8, 15), LocalTime.of(21, 30), "Firenze",
                new BigDecimal("43.7696"), new BigDecimal("11.2558"), 480, 4),
            new DatiUtente("alice_moretti", "alice.moretti@demo.it", Ruolo.PREMIUM, SegnoZodiacaleEnum.TORO,
                SegnoZodiacaleEnum.CANCRO, LocalDate.of(1994, 5, 6), LocalTime.of(6, 50), "Genova",
                new BigDecimal("44.4056"), new BigDecimal("8.9463"), 810, 9),
            new DatiUtente("davide_esposito", "davide.esposito@demo.it", Ruolo.BASE, SegnoZodiacaleEnum.GEMELLI,
                SegnoZodiacaleEnum.VERGINE, LocalDate.of(1999, 6, 11), LocalTime.of(14, 15), "Padova",
                new BigDecimal("45.4064"), new BigDecimal("11.8768"), 290, 2),
            new DatiUtente("sara_greco", "sara.greco@demo.it", Ruolo.BASE, SegnoZodiacaleEnum.CANCRO,
                SegnoZodiacaleEnum.TORO, LocalDate.of(1993, 7, 3), LocalTime.of(22, 5), "Palermo",
                new BigDecimal("38.1157"), new BigDecimal("13.3615"), 560, 7),
            new DatiUtente("matteo_russo", "matteo.russo@demo.it", Ruolo.PREMIUM,
                SegnoZodiacaleEnum.SAGITTARIO, SegnoZodiacaleEnum.LEONE, LocalDate.of(1991, 12, 5),
                LocalTime.of(10, 40), "Verona", new BigDecimal("45.4384"), new BigDecimal("10.9916"), 930, 12),
            new DatiUtente("noemi_galli", "noemi.galli@demo.it", Ruolo.BASE, SegnoZodiacaleEnum.VERGINE,
                SegnoZodiacaleEnum.BILANCIA, LocalDate.of(2000, 9, 18), LocalTime.of(16, 25), "Bari",
                new BigDecimal("41.1171"), new BigDecimal("16.8719"), 410, 3));

        for (DatiUtente dati : utentiDemo) {
            Utente utente = inizializzaUtente(dati);
            inizializzaContenuti(utente, sogniDemo(dati.username()));
        }

        System.out.println("Profilo ADMIN completo disponibile.");
    }

    private Utente inizializzaUtente(DatiUtente dati) {
        Optional<Utente> utenteEsistente = utenteRepository.findByUsername(dati.username());
        if (utenteEsistente.isEmpty()) {
            utenteEsistente = utenteRepository.findByUsername(usernameLegacy(dati.username()));
        }
        Utente utente = utenteEsistente.orElseGet(Utente::new);
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
        if (dati.ruolo() == Ruolo.PREMIUM) {
            utente.setPremiumAttivatoAt(LocalDateTime.now().minusDays(30));
        }
        utente = utenteRepository.save(utente);
        inizializzaTemaNatale(utente, dati);
        return utente;
    }

    private String usernameLegacy(String username) {
        return switch (username) {
            case "sofia_rossi" -> "sofia_pesci";
            case "marco_bianchi" -> "marco_ariete";
            case "elisa_romano" -> "ely_acquario";
            case "luca_ferrari" -> "luca_leone";
            case "giulia_conti" -> "giulia_leone";
            case "alice_moretti" -> "alice_toro";
            case "davide_esposito" -> "davide_gemelli";
            case "sara_greco" -> "sara_cancro";
            case "matteo_russo" -> "matteo_sagittario";
            case "noemi_galli" -> "noemi_vergine";
            default -> username;
        };
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

    private void inizializzaContenuti(Utente utente, List<DatiSogno> sogni) {
        for (DatiSogno dati : sogni) {
            Sogno sogno = sognoRepository.findByUtenteId(utente.getId()).stream()
                    .filter(esistente -> esistente.getTesto().equals(dati.testo()))
                    .findFirst()
                    .orElseGet(() -> sognoRepository.save(Sogno.builder().utente(utente).testo(dati.testo()).build()));
            if (interpretazioneRepository.findBySognoId(sogno.getId()).isEmpty()) {
                interpretazioneRepository.save(Interpretazione.builder()
                        .sogno(sogno)
                        .prompt("Interpretazione del sogno")
                        .testo(dati.interpretazione())
                        .umore(dati.umore())
                        .stile(dati.stile())
                        .build());
            }
        }
    }

    private List<DatiSogno> sogniDemo(String username) {
        return List.of(
                new DatiSogno("Ho sognato una porta che si apriva su un giardino segreto.",
                        "La porta suggerisce una nuova possibilita, mentre il giardino richiama crescita e cura.",
                        UmoreEnum.SERENO, StileEnum.SIMBOLICO),
                new DatiSogno("Ho sognato di volare sopra la mia citta.",
                        "Volare puo indicare desiderio di liberta e una prospettiva piu ampia sulla propria vita.",
                        username.equals("marco_bianchi") ? UmoreEnum.INTENSO : UmoreEnum.SERENO,
                        StileEnum.JUNGHIANO));
    }

    private record DatiUtente(String username, String email, Ruolo ruolo, SegnoZodiacaleEnum segno,
            SegnoZodiacaleEnum ascendente, LocalDate dataNascita, LocalTime oraNascita, String luogoNascita,
            BigDecimal latitudine, BigDecimal longitudine, int qi, int giorniConsecutivi) {
    }

    private record DatiSogno(String testo, String interpretazione, UmoreEnum umore, StileEnum stile) {
    }
}
