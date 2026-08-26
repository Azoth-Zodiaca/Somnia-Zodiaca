package com.azoth.somniazodiaca.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.azoth.somniazodiaca.dtos.RichiestaInterpretazioneDto;
import com.azoth.somniazodiaca.config.AppDomainProperties;
import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.dtos.records.InterpretazioneRequest;
import com.azoth.somniazodiaca.dtos.records.RendiPermanenteRequest;
import com.azoth.somniazodiaca.dtos.records.SalvaInterpretazioneRequest;
import com.azoth.somniazodiaca.enums.Ruolo;
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
        private final AppDomainProperties appDomainProperties;

        public OracoloController(
                        GeminiService geminiService,
                        UtenteService utenteService,
                        InterpretazioneService interpretazioneService,
                        OracoloService oracoloService,
                        AppDomainProperties appDomainProperties) {

                this.geminiService = geminiService;
                this.utenteService = utenteService;
                this.interpretazioneService = interpretazioneService;
                this.oracoloService = oracoloService;
                this.appDomainProperties = appDomainProperties;
        }

        @GetMapping("/oracolo")
        public String redirectOracolo() {
                return "redirect:/app/oracolo";
        }

        @GetMapping("/app/oracolo")
        public String oracolo(
                        Authentication authentication,
                        Model model,
                        @RequestParam(required = false) String testoSogno,
                        @RequestParam(required = false) Long interpretazioneId,
                        @RequestParam(required = false) String azione) {

                UtenteDetail utente = utenteService
                                .findByUsername(authentication.getName())
                                .orElseThrow();

                List<RichiestaInterpretazioneDto> interpretazioni = interpretazioneService
                                .findByUtenteId(utente.getId());

                model.addAttribute("interpretazioni", interpretazioni);
                model.addAttribute("premiumTemaNatale",
                                !"BASE".equals(utente.getRuolo().name()));

                model.addAttribute("testoSogno", testoSogno);
                model.addAttribute("interpretazioneId", interpretazioneId);
                model.addAttribute("azione", azione);

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

                if (richiesta.testoSogno().length() > appDomainProperties.getOracolo().getLimiteSognoCaratteri()) {
                        throw new IllegalArgumentException("Il testo del sogno supera il limite configurato");
                }

                oracoloService.verificaQiDisponibili(authentication.getName());

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

                        if (utente.getRuolo() == Ruolo.BASE) {
                                throw new IllegalStateException(
                                                "La personalizzazione con il tema natale è riservata agli utenti Premium");
                        }

                        aggiungiTemaNatale(prompt, utente.getTemaNatale());
                }

                prompt.append("\n\nLimita la risposta a un massimo di ")
                                .append(appDomainProperties.getOracolo().getLimiteInterpretazioneCaratteri())
                                .append(" caratteri.\n");

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

        @PostMapping(value = "/app/oracolo/salva", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public Map<String, Object> salvaInterpretazione(
                        @RequestBody SalvaInterpretazioneRequest richiesta,
                        Authentication authentication) {

                Long id = oracoloService.salvaInterpretazione(
                                authentication.getName(),
                                richiesta);

                Integer qi = utenteService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"))
                                .getQi();

                return Map.of("id", id, "qi", qi);
        }

        @PostMapping(value = "/app/oracolo/rendi-permanente", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public Map<String, Integer> rendiPermanente(
                        @RequestBody RendiPermanenteRequest richiesta,
                        Authentication authentication) {

                oracoloService.rendiPermanente(
                                authentication.getName(),
                                richiesta.interpretazioneId());

                Integer qi = utenteService.findByUsername(authentication.getName())
                                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"))
                                .getQi();

                return Map.of("qi", qi);
        }

        @PostMapping(value = "/app/oracolo/cancella", consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public void cancellaInterpretazione(
                        @RequestBody RendiPermanenteRequest richiesta,
                        Authentication authentication) {

                oracoloService.cancellaInterpretazione(
                                authentication.getName(),
                                richiesta.interpretazioneId());
        }
}
