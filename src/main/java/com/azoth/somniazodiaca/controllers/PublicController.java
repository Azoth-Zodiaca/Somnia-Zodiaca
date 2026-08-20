// ESEMPIO di Controller Spring Boot per servire le pagine Thymeleaf.
// Copia questo file (o il suo contenuto) nel tuo package "controller"
// e adatta i nomi dei metodi/attributi al tuo progetto reale.
//
// Nota per principianti: ogni metodo restituisce una STRINGA che è il
// percorso del file .html dentro templates/ (senza estensione .html).
// Es: "app/dashboard" -> templates/app/dashboard.html

package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    @GetMapping("/")
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

@Controller
class AuthController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @GetMapping("/onboarding")
    public String onboarding() {
        return "onboarding";
    }

    // Esempio: gestione dell'invio del form di onboarding
    // @PostMapping("/onboarding/dati-nascita")
    // public String salvaDatiNascita(@ModelAttribute DatiNascitaForm form) {
    //     // qui salvi i dati e calcoli il tema natale...
    //     return "redirect:/app/dashboard";
    // }
}

@Controller
class AppController {

    // Tutte le pagine dell'area riservata iniziano con /app/...
    // In un progetto reale andrebbero protette con Spring Security,
    // in modo che solo un utente autenticato possa vederle.

    @GetMapping("/app/dashboard")
    public String dashboard(Model model) {
        // Esempio di come passare dati reali alla pagina:
        // model.addAttribute("nomeUtente", utenteCorrente.getNome());
        // model.addAttribute("saldoQi", utenteCorrente.getSaldoQi());
        return "app/dashboard";
    }

    @GetMapping("/app/profilo")
    public String profilo() {
        return "app/profilo";
    }

    @GetMapping("/app/tema-natale")
    public String temaNatale() {
        return "app/tema-natale";
    }

    @GetMapping("/app/oracolo")
    public String oracolo() {
        return "app/oracolo";
    }

    @GetMapping("/app/social")
    public String social() {
        return "app/social";
    }

    @GetMapping("/app/shop")
    public String shop() {
        return "app/shop";
    }

    @GetMapping("/app/inventario")
    public String inventario() {
        return "app/inventario";
    }

    @GetMapping("/app/wallet")
    public String wallet() {
        return "app/wallet";
    }

    @GetMapping("/app/progressi")
    public String progressi() {
        return "app/progressi";
    }

    @GetMapping("/app/impostazioni")
    public String impostazioni() {
        return "app/impostazioni";
    }
}
