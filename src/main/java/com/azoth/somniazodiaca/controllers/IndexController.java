package com.azoth.somniazodiaca.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"/", "/index"})
    public String home(Model model) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("sognatori", 12000);
        stats.put("lettureGratuite", 3);
        stats.put("ricompensaGiornaliera", 50);

        Map<String, Object> cielo = new HashMap<>();
        cielo.put("lunaLabel", "☾ Luna in Bilancia");
        cielo.put("transitoLabel", "Mercurio retrogrado");

        model.addAttribute("stats", stats);
        model.addAttribute("cielo", cielo);
        model.addAttribute("pilastri", List.of());
        model.addAttribute("posts", List.of());
        return "landing/index";
    }

}
