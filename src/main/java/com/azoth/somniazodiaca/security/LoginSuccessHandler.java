package com.azoth.somniazodiaca.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.azoth.somniazodiaca.services.UtenteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final UtenteService utenteService;

    public LoginSuccessHandler(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        utenteService.registraAccesso(authentication.getName());

        response.sendRedirect("/app/dashboard");
    }
}