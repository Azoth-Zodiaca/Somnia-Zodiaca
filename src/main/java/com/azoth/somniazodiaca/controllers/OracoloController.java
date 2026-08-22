package com.azoth.somniazodiaca.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.azoth.somniazodiaca.dtos.RichiestaInterpretazioneDto;
import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.dtos.records.InterpretazioneRequest;
import com.azoth.somniazodiaca.dtos.records.RendiPermanenteRequest;
import com.azoth.somniazodiaca.dtos.records.SalvaInterpretazioneRequest;
import com.azoth.somniazodiaca.services.GeminiService;
import com.azoth.somniazodiaca.services.InterpretazioneService;
import com.azoth.somniazodiaca.services.OracoloService;
import com.azoth.somniazodiaca.services.UtenteService;

@Controller
public class OracoloController {

        private final GeminiService geminiService;
        private final UtenteService utenteService;
        private final InterpretazioneService interpretazioneService;
        private final OracoloService oracoloService;

        public OracoloController(
                        GeminiService geminiService,
                        UtenteService utenteService,
                        InterpretazioneService interpretazioneService,
                        OracoloService oracoloService) {

                this.geminiService = geminiService;
                this.utenteService = utenteService;
                this.interpretazioneService = interpretazioneService;
                this.oracoloService = oracoloService;
        }

        @GetMapping("/oracolo")
        public String redirectOracolo() {
                return "redirect:/app/oracolo";
        }

        @GetMapping("/app/oracolo")
        public String oracolo(
                        Authentication authentication,
                        Model model) {

                UtenteDetail utente = utenteService
                                .findByUsername(authentication.getName())
                                .orElseThrow();

                List<RichiestaInterpretazioneDto> interpretazioni = interpretazioneService
                                .findByUtenteId(utente.getId());

                model.addAttribute("interpretazioni", interpretazioni);

                return "app/oracolo";
        }

        @PostMapping(value = "/app/oracolo/interpreta", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
        @ResponseBody
        public String interpreta(
                        @RequestBody InterpretazioneRequest richiesta,
                        Authentication authentication) {

                if (richiesta.testoSogno() == null
                                || richiesta.testoSogno().isBlank()) {
                        throw new IllegalArgumentException("Il testo del sogno è obbligatorio");
                }

                StringBuilder prompt = new StringBuilder();

                prompt.append("""
                                Interpreta il seguente sogno in italiano.
                                Non presentare l'interpretazione come una previsione certa.
                                Fornisci una risposta empatica, simbolica e comprensibile.

                                Sogno:
                                """);
                prompt.append(richiesta.testoSogno());

                prompt.append("\n\nUmore provato: ");
                prompt.append(richiesta.umore());

                prompt.append("\nStile richiesto: ");
                prompt.append(richiesta.stile());

                if (richiesta.usaTemaNatale()) {
                        UtenteDetail utente = utenteService
                                        .findByUsername(authentication.getName())
                                        .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

                        aggiungiTemaNatale(prompt, utente.getTemaNatale());
                }

                prompt.append("""

                                Limita la risposta a un massimo di 1000 caratteri.
                                """);

                return geminiService.askGemini(prompt.toString());
        }

        private void aggiungiTemaNatale(
                        StringBuilder prompt,
                        TemaNataleDto temaNatale) {

                if (temaNatale == null) {
                        prompt.append("""

                                        Il tema natale non è disponibile.
                                        Interpreta il sogno senza personalizzazione astrologica.
                                        """);
                        return;
                }

                prompt.append("""

                                Dati del tema natale dell'utente:
                                """);
                prompt.append("Data di nascita: ")
                                .append(temaNatale.getDataNascita())
                                .append("\nOra di nascita: ")
                                .append(temaNatale.getOraNascita())
                                .append("\nLuogo di nascita: ")
                                .append(temaNatale.getLuogoNascita());
        }

        @PostMapping(value = "/app/oracolo/salva", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
        @ResponseBody
        public String salvaInterpretazione(
                        @RequestBody SalvaInterpretazioneRequest richiesta,
                        Authentication authentication) {

                Long id = oracoloService.salvaInterpretazione(
                                authentication.getName(),
                                richiesta);

                return String.valueOf(id);
        }

        @PostMapping(value = "/app/oracolo/rendi-permanente", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
        @ResponseBody
        public String rendiPermanente(
                        @RequestBody RendiPermanenteRequest richiesta,
                        Authentication authentication) {

                oracoloService.rendiPermanente(
                                authentication.getName(),
                                richiesta.interpretazioneId());

                return "Interpretazione resa permanente";
        }
}
