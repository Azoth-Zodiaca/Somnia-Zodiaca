package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImpostazioniController {

    @GetMapping("/impostazioni")
    public String impostazioni() {
        return "impostazioni/impostazioni";
    }
}
