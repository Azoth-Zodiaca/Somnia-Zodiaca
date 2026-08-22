package com.azoth.somniazodiaca.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.services.UtenteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ImpostazioniController {

    private final UtenteService utenteService;

    public ImpostazioniController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/impostazioni")
    public String redirectImpostazioni() {
        return "redirect:/app/impostazioni";
    }

    @GetMapping("/app/impostazioni")
    public String impostazioni(Authentication authentication, Model model) {
        UtenteDetail utente = utenteService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Utente autenticato non trovato"));

        model.addAttribute("username", utente.getUsername());
        model.addAttribute("email", utente.getEmail());

        return "app/impostazioni";
    }

    @PostMapping("/app/impostazioni/account")
    public String aggiornaAccount(
            Authentication authentication,
            @RequestParam String username,
            @RequestParam String email) {

        UtenteDetail utente = utenteService.updateAccount(
                authentication.getName(),
                username,
                email);

        Authentication nuovaAuthentication = new UsernamePasswordAuthenticationToken(
                utente.getUsername(),
                authentication.getCredentials(),
                authentication.getAuthorities());

        SecurityContextHolder.getContext()
                .setAuthentication(nuovaAuthentication);

        return "redirect:/app/impostazioni";
    }

    @PostMapping("/app/impostazioni/password")
    public String cambiaPassword(
            Authentication authentication,
            @RequestParam String passwordAttuale,
            @RequestParam String nuovaPassword,
            @RequestParam String confermaPassword) {

        utenteService.changePassword(
                authentication.getName(),
                passwordAttuale,
                nuovaPassword,
                confermaPassword);

        return "redirect:/app/impostazioni";
    }

    @PostMapping("/app/impostazioni/account/elimina")
    public String eliminaAccount(Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) {
        utenteService.deleteAccount(authentication.getName());

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/";
    }
}
