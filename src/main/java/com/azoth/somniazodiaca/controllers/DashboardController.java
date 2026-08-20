package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String redirectDashboard() {
        return "redirect:/app/dashboard";
    }

    @GetMapping("/app/dashboard")
    public String dashboard(Model model) {
        // Esempio di come passare dati reali alla pagina:
        // model.addAttribute("nomeUtente", utenteCorrente.getNome());
        // model.addAttribute("saldoQi", utenteCorrente.getSaldoQi());
        return "app/dashboard";
    }
    

}
