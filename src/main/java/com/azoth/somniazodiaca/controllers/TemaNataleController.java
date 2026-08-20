package com.azoth.somniazodiaca.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

@Controller
public class TemaNataleController {

    private final UtenteRepository utenteRepository;

    public TemaNataleController(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @GetMapping("/tema-natale")
    public String redirectTemaNatale() {
        return "redirect:/app/tema-natale";
    }

    @GetMapping("/app/tema-natale")
    public String temaNatale() {
        return "app/tema-natale";
    }
}
