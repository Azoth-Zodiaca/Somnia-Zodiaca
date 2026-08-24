package com.azoth.somniazodiaca.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.repositories.UtenteRepository;
import com.azoth.somniazodiaca.services.AstroWayService;
import com.azoth.somniazodiaca.services.TemaNataleService;
import com.azoth.somniazodiaca.services.TemaNataleViewService;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class TemaNataleController {

        private final UtenteRepository utenteRepository;
        private final TemaNataleService temaNataleService;
        private final AstroWayService astroWayService;
        private final TemaNataleViewService temaNataleViewService;

        public TemaNataleController(
                        UtenteRepository utenteRepository,
                        TemaNataleService temaNataleService,
                        AstroWayService astroWayService,
                        TemaNataleViewService temaNataleViewService) {

                this.utenteRepository = utenteRepository;
                this.temaNataleService = temaNataleService;
                this.astroWayService = astroWayService;
                this.temaNataleViewService = temaNataleViewService;
        }

        @GetMapping("/tema-natale")
        public String redirectTemaNatale() {
                return "redirect:/app/tema-natale";
        }

        @GetMapping("/app/tema-natale")
        public String temaNatale(
                        Authentication authentication,
                        Model model) {

                Utente utente = utenteRepository
                                .findByUsername(authentication.getName())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Utente non trovato"));

                TemaNataleDto temaNatale = temaNataleService
                                .findByUtenteId(utente.getId())
                                .orElse(null);

                model.addAttribute("temaNatale", temaNatale);

                if (temaNatale != null
                                && temaNatale.getRispostaAstroWay() != null) {

                        JsonNode temaChart = astroWayService.parseChart(
                                        temaNatale.getRispostaAstroWay());

                        if (!temaChart.path("ok").asBoolean(false)) {
                                throw new IllegalStateException(
                                                "AstroWay ha restituito una risposta non valida");
                        }

                        JsonNode datiTema = temaChart.path("data");

                        JsonNode houses = datiTema.path("houses");
                        double ascendente = houses.path("ascendant").asDouble();

                        model.addAttribute(
                                        "ascendenteSegno",
                                        temaNataleViewService.segnoDaLongitudine(ascendente));

                        model.addAttribute(
                                        "ascendenteSimbolo",
                                        temaNataleViewService.simboloDaSegno(
                                                        temaNataleViewService.segnoDaLongitudine(ascendente)));

                        model.addAttribute("temaChart", datiTema);
                        model.addAttribute(
                                        "pianeti",
                                        temaNataleViewService.estraiPianeti(datiTema));
                }

                return "app/tema-natale";
        }
}