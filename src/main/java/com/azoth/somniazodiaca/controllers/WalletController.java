package com.azoth.somniazodiaca.controllers;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.services.UtenteService;

@Controller
public class WalletController {

    private final UtenteService utenteService;

    public WalletController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/wallet")
    public String redirectWallet() {
        return "redirect:/app/wallet";
    }

    @GetMapping("/app/wallet")
    public String wallet(Authentication authentication, Model model) {
        UtenteDetail utente = utenteService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        model.addAttribute("utente", utente);
        int giorniRicompense = utente.getGiorniRicompensaGiornaliera() == null
                ? 0
                : utente.getGiorniRicompensaGiornaliera();
        model.addAttribute("giorniRicompense", giorniRicompense);
        model.addAttribute("giornoRicompensa", Math.min(giorniRicompense + 1, 7));
        model.addAttribute(
                "ricompensaRiscossa",
                LocalDate.now().equals(
                        utente.getUltimaRicompensaGiornaliera()));
        return "app/wallet";
    }

    @PostMapping("/app/wallet/ricarica")
    public String ricarica(Authentication authentication,
            @RequestParam int quantitaQi) {
        utenteService.addQi(authentication.getName(), quantitaQi);
        return "redirect:/app/wallet";
    }

    @PostMapping("/app/wallet/ricompensa")
    public String riscuotiRicompensa(Authentication authentication) {
        utenteService.riscuotiRicompensaGiornaliera(
                authentication.getName());

        return "redirect:/app/wallet";
    }
}