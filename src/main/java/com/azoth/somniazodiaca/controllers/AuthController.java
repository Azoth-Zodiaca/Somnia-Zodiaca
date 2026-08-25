package com.azoth.somniazodiaca.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azoth.somniazodiaca.dtos.records.Registrazione;
import com.azoth.somniazodiaca.exceptions.EmailAlreadyExistsException;
import com.azoth.somniazodiaca.exceptions.UsernameAlreadyExistsException;
import com.azoth.somniazodiaca.services.UtenteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
    private final UtenteService utenteService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(
            UtenteService utenteService,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository) {

        this.utenteService = utenteService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
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
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) { // Aggiunto il parametro Model per passare i messaggi alla vista

        try {
            // Tentativo di registrazione dell'utente
            utenteService.register(new Registrazione(username, email, password));

        } catch (UsernameAlreadyExistsException e) {
            // Gestione username già occupato
            model.addAttribute("errorMessage", "Username già esistente.");
            return "auth/register"; // Ritorna alla pagina di registrazione senza fare redirect

        } catch (EmailAlreadyExistsException e) {
            // Gestione email già occupata
            model.addAttribute("errorMessage", "Email già esistente.");
            return "auth/register"; // Ritorna alla pagina di registrazione senza fare redirect
        }

        // Autenticazione automatica dopo la registrazione andata a buon fine
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(
                context,
                request,
                response);

        // Redirect impostato sulla dashboard
        return "redirect:/app/dashboard";
    }

}
