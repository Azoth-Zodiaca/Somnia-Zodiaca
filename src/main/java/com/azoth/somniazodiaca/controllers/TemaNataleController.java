package com.azoth.somniazodiaca.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.repositories.UtenteRepository;
import com.azoth.somniazodiaca.services.TemaNataleService;

@Controller
public class TemaNataleController {

    private final UtenteRepository utenteRepository;
    private final TemaNataleService temaNataleService;

    public TemaNataleController(
            UtenteRepository utenteRepository,
            TemaNataleService temaNataleService) {

        this.utenteRepository = utenteRepository;
        this.temaNataleService = temaNataleService;
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
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        temaNataleService.findByUtenteId(utente.getId())
                .ifPresent(tema -> model.addAttribute("temaNatale", tema));

        return "app/tema-natale";
    }
    
}