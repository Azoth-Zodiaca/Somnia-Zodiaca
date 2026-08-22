package com.azoth.somniazodiaca.controllers;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.dtos.records.InterpretazioneRequest;
import com.azoth.somniazodiaca.services.GeminiService;
import com.azoth.somniazodiaca.services.UtenteService;

@Controller
public class OracoloController {

    private final GeminiService geminiService;
    private final UtenteService utenteService;

    public OracoloController(
            GeminiService geminiService,
            UtenteService utenteService) {

        this.geminiService = geminiService;
        this.utenteService = utenteService;
    }

    @GetMapping("/oracolo")
    public String redirectOracolo() {
        return "redirect:/app/oracolo";
    }

    @GetMapping("/app/oracolo")
    public String oracolo() {
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
}
