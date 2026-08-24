package com.azoth.somniazodiaca.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.azoth.somniazodiaca.dtos.BadgeViewDto;
import com.azoth.somniazodiaca.services.BadgeService;

@Controller
public class ProgressiController {

    private final BadgeService badgeService;

    public ProgressiController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping("/progressi")
    public String redirectProgressi() {
        return "redirect:/app/progressi";
    }

    @GetMapping("/app/progressi")
    public String progressi(
            Authentication authentication,
            Model model) {

        List<BadgeViewDto> badge = badgeService
                .getProgressi(authentication.getName());

        long sbloccati = badge.stream()
                .filter(BadgeViewDto::sbloccato)
                .count();

        model.addAttribute("badge", badge);
        model.addAttribute("sbloccati", sbloccati);
        model.addAttribute("totaleBadge", badge.size());

        return "app/progressi";
    }
}
