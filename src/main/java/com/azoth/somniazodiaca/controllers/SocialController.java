package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SocialController {

    @GetMapping("/social")
    public String redirectSocial() {
        return "redirect:/app/social";
    }

    @GetMapping("/app/social")
    public String social() {
        return "app/social";
    }
}
