package com.azoth.somniazodiaca.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.services.UtenteService;

@Controller
public class ProfiloController {

    private final UtenteService utenteService;

    public ProfiloController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/profilo")
    public String redirectProfilo() {
        return "redirect:/app/profilo";
    }

    @GetMapping("/app/profilo")
    public String profilo(Authentication authentication, Model model) {
        UtenteDetail utente = utenteService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Utente autenticato non trovato"));

        model.addAttribute("username", utente.getUsername());
        model.addAttribute("ruolo", utente.getRuolo());
        model.addAttribute("saldoQI", utente.getQi());
        model.addAttribute("profiloSegnoZodiacale", utente.getSegnoZodiacale());
        model.addAttribute("profiloAscendente", utente.getAscendente());
        model.addAttribute("dataRegistrazione", utente.getDataRegistrazione());
        model.addAttribute("ultimoAccesso", utente.getUltimoAccesso());
        model.addAttribute("profiloColore", utente.getProfiloColore());
        return "app/profilo";
    }

}
