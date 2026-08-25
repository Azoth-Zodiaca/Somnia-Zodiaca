package com.azoth.somniazodiaca.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azoth.somniazodiaca.dtos.records.AstroWayChartRequest;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.repositories.UtenteRepository;
import com.azoth.somniazodiaca.services.AstroWayService;
import com.azoth.somniazodiaca.services.TemaNataleService;

@Controller
public class OnboardingController {

        private final UtenteRepository utenteRepository;
        private final TemaNataleService temaNataleService;
        private final AstroWayService astroWayService;

        public OnboardingController(
                        UtenteRepository utenteRepository,
                        TemaNataleService temaNataleService,
                        AstroWayService astroWayService) {

                this.utenteRepository = utenteRepository;
                this.temaNataleService = temaNataleService;
                this.astroWayService = astroWayService;
        }

        @PostMapping("/onboarding/dati-nascita")
        public String datiNascita(
                        @RequestParam LocalDate dataNascita,
                        @RequestParam(required = false) LocalTime oraNascita,
                        @RequestParam String luogoNascita,
                        @RequestParam Long geonameId,
                        @RequestParam BigDecimal latitudine,
                        @RequestParam BigDecimal longitudine,
                        @RequestParam String timezone,
                        Authentication authentication) {

                if (oraNascita == null) {
                        oraNascita = LocalTime.NOON;
                }

                Utente utente = utenteRepository
                                .findByUsername(authentication.getName())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Utente non trovato"));

                ZoneId zoneId = ZoneId.of(timezone);

                LocalDateTime dataOraLocale = LocalDateTime.of(dataNascita, oraNascita);

                ZoneOffset offset = zoneId.getRules().getOffset(dataOraLocale);

                double timezoneOffset = offset.getTotalSeconds() / 3600.0;

                String oraFormattata = oraNascita
                                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                AstroWayChartRequest richiesta = new AstroWayChartRequest(
                                dataNascita.toString(),
                                oraFormattata,
                                timezoneOffset,
                                latitudine.doubleValue(),
                                longitudine.doubleValue(),
                                "P");

                String rispostaAstroWay = astroWayService.calculateChart(richiesta);

                temaNataleService.creaTemaNatale(
                                utente,
                                dataNascita,
                                oraNascita,
                                luogoNascita,
                                geonameId,
                                latitudine,
                                longitudine,
                                timezone,
                                rispostaAstroWay);

                return "redirect:/app/tema-natale";
        }

        @PostMapping("/onboarding/salta")
        public String saltaOnboarding() {
                return "redirect:/app/dashboard";
        }
}