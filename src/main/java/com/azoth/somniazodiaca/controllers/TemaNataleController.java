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
    public String temaNatale(Model model, Authentication auth) {

        String username = auth.getName(); 
        // username dell’utente loggato, serve per fare una query
        // è marginale ai fini della performance, anzi è pratica usuale in progetti con la security

        Utente u = utenteRepository.findByUsername(username)
                .orElseThrow(); // impossibile fallire se loggato

        model.addAttribute("input", TemaNataleDto.builder()
                .utenteId(u.getId())
                .build());

        return "tema-natale/tema-natale";
    }
}
