package com.azoth.somniazodiaca.controllers;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.azoth.somniazodiaca.dtos.CreazioneUtenteDto;
import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.services.UtenteService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class AuthController {

    private final UtenteService utenteService;

    public AuthController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    private Optional<Long> getCurrentUserId(HttpSession session) {
        Object value = session.getAttribute("currentUserId");
        if (value instanceof Long) {
            return Optional.of((Long) value);
        }
        if (value instanceof Integer) {
            return Optional.of(((Integer) value).longValue());
        }
        return Optional.empty();
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/registrazione")
    public String registrationPage(HttpSession session, Model model) {
        if (getCurrentUserId(session).isPresent()) {
            return "redirect:/profilo";
        }
        model.addAttribute("creazioneUtenteDto", new CreazioneUtenteDto());
        return "registrazione";
    }

    // @PostMapping("/registrazione")
    // public String register(
    //         @Valid CreazioneUtenteDto dto,
    //         BindingResult bindingResult,
    //         Model model,
    //         HttpSession session) {

    //     dto.setRuolo(Ruolo.BASE);

    //     if (utenteService.findByUsername(dto.getUsername()).isPresent()) {
    //         bindingResult.rejectValue("username", "error.username", "Username già in uso");
    //     }

    //     if (utenteService.findByEmail(dto.getEmail()).isPresent()) {
    //         bindingResult.rejectValue("email", "error.email", "Email già in uso");
    //     }

    //     if (bindingResult.hasErrors()) {
    //         return "registrazione";
    //     }

    //     UtenteDetail saved = utenteService.register(dto);
    //     session.setAttribute("currentUserId", saved.getId());
    //     return "redirect:/profilo";
    // }
}
