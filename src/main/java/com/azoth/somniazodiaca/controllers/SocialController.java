package com.azoth.somniazodiaca.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.services.InterpretazioneService;
import com.azoth.somniazodiaca.services.PostService;
import com.azoth.somniazodiaca.services.UtenteService;

@Controller
public class SocialController {

    private final PostService postService;
    private final InterpretazioneService interpretazioneService;
    private final UtenteService utenteService;

    public SocialController(
            PostService postService,
            InterpretazioneService interpretazioneService,
            UtenteService utenteService) {

        this.postService = postService;
        this.interpretazioneService = interpretazioneService;
        this.utenteService = utenteService;
    }

    @GetMapping("/social")
    public String redirectSocial() {
        return "redirect:/app/social";
    }

    @GetMapping("/app/social")
    public String social(
            Authentication authentication,
            Model model) {

        UtenteDetail utente = utenteService
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException(
                        "Utente autenticato non trovato"));

        model.addAttribute(
                "feed",
                postService.findFeed(authentication.getName()));

        model.addAttribute(
                "mieInterpretazioni",
                interpretazioneService.findByUtenteId(utente.getId()));

        model.addAttribute(
                "feed",
                postService.findFeed(authentication.getName()));

        model.addAttribute(
                "feedMioSegno",
                postService.findFeedMioSegno(authentication.getName()));

        return "app/social";
    }

    @PostMapping("/app/social/post")
    public String creaPost(
            Authentication authentication,
            @RequestParam Long interpretazioneId,
            @RequestParam String testoVisibile) {

        postService.creaPost(
                authentication.getName(),
                interpretazioneId,
                testoVisibile);

        return "redirect:/app/social";
    }

    @PostMapping("/app/social/post/{postId}/like")
    public String toggleLike(
            Authentication authentication,
            @PathVariable Long postId) {

        postService.toggleLike(authentication.getName(), postId);

        return "redirect:/app/social";
    }
}