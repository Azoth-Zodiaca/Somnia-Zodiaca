package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azoth.somniazodiaca.dtos.records.Registrazione;
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
    // return "error/access-denied";
    // }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @GetMapping("/onboarding")
    public String onboarding() {
        return "onboarding";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {

        utenteService.register(
                new Registrazione(username, email, password));

        return "redirect:/onboarding";
    }

}
