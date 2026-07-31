package com.azoth.somniazodiaca.controllers;

import java.util.Optional;

import com.azoth.somniazodiaca.dtos.CreazioneUtenteDto;
import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.services.UtenteService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UtenteService utenteService;

    public AuthController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String usernameOrEmail,
            @RequestParam String password,
            Model model) {

        Optional<UtenteDetail> utente = utenteService.authenticate(usernameOrEmail, password);
        if (utente.isEmpty()) {
            model.addAttribute("loginError", "Credenziali non valide");
            return "login";
        }

        model.addAttribute("utente", utente.get());
        return "profilo";
    }

    @GetMapping("/registrazione")
    public String registrationPage(Model model) {
        model.addAttribute("creazioneUtenteDto", new CreazioneUtenteDto());
        return "registrazione";
    }

    @PostMapping("/registrazione")
    public String register(
            @Valid CreazioneUtenteDto dto,
            BindingResult bindingResult,
            Model model) {

        dto.setRuolo(Ruolo.BASE);

        if (utenteService.findByUsername(dto.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.username", "Username già in uso");
        }

        if (utenteService.findByEmail(dto.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.email", "Email già in uso");
        }

        if (bindingResult.hasErrors()) {
            return "registrazione";
        }

        UtenteDetail saved = utenteService.register(dto);
        model.addAttribute("utente", saved);
        return "profilo";
    }
}