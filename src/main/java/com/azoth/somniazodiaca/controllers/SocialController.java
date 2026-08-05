package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SocialController {

    @GetMapping("/social")
    public String social() {
        return "social/social";
    }
}
