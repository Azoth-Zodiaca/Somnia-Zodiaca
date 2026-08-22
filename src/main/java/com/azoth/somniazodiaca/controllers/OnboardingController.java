package com.azoth.somniazodiaca.controllers;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OnboardingController {

    @PostMapping("/onboarding/dati-nascita")
    public String datiNascita(
            @RequestParam LocalDate dataNascita,
            @RequestParam LocalTime oraNascita,
            @RequestParam String luogoNascita,
            @RequestParam Long geonameId,
            @RequestParam String latitudine,
            @RequestParam String longitudine,
            @RequestParam String timezone,
            Authentication authentication) {

        return "redirect:/app/tema-natale";
    }
}