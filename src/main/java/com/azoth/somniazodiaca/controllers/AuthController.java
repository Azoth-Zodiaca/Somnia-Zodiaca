package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.azoth.somniazodiaca.dtos.records.Registrazione;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/accesso-negato")
    public String accessDenied() {
        return "error/access-denied";
    }

    @GetMapping("/registrazione")
    public String paginaRegistrazione(Model model) {

        model.addAttribute(
                "registrazione",
                new Registrazione("", "", ""));

        return "auth/registrazione";
    }

}
