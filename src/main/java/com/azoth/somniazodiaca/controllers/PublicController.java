// ESEMPIO di Controller Spring Boot per servire le pagine Thymeleaf.
// Copia questo file (o il suo contenuto) nel tuo package "controller"
// e adatta i nomi dei metodi/attributi al tuo progetto reale.
//
// Nota per principianti: ogni metodo restituisce una STRINGA che è il
// percorso del file .html dentro templates/ (senza estensione .html).
// Es: "app/dashboard" -> templates/app/dashboard.html

package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    @GetMapping({"/", "/index", "/home"})
    public String home() {
        return "index"; // templates/index.html
    }

    @GetMapping("/premium")
    public String premium() {
        return "premium";
    }

    @GetMapping("/enciclopedia")
    public String enciclopedia() {
        return "enciclopedia";
    }

    @GetMapping("/legal/privacy")
    public String privacy() {
        return "legal/privacy";
    }

    @GetMapping("/legal/termini")
    public String termini() {
        return "legal/termini";
    }
}
