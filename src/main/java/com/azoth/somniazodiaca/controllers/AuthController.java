package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.azoth.somniazodiaca.dtos.records.Registrazione;
import com.azoth.somniazodiaca.exceptions.EmailAlreadyExistsException;
import com.azoth.somniazodiaca.exceptions.UsernameAlreadyExistsException;
import com.azoth.somniazodiaca.services.UtenteService;

@Controller
public class AuthController {
    private final UtenteService utenteService;

    public AuthController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // @GetMapping("/accesso-negato")
    // public String accessDenied() {
    //     return "error/access-denied";
    // }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }


    @GetMapping("/onboarding")
    public String onboarding() {
        return "onboarding";
    }

    @PostMapping("/register") // utile in sede di registrazione
    public String register(@ModelAttribute("registrazione") Registrazione dto, Model model) {
        try {
            utenteService.register(dto);
            return "redirect:/login";
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException e) {
            model.addAttribute("errore", e.getMessage());
            return "auth/registrazione";
        }
    }

}
