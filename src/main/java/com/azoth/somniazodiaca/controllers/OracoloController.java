package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OracoloController {

    @GetMapping("/oracolo")
    public String redirectOracolo() {
        return "redirect:/app/oracolo";
    }

    @GetMapping("/app/oracolo")
    public String oracolo() {
        return "app/oracolo";
    }
}
