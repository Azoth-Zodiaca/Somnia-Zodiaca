package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfiloController {

    @GetMapping("/profilo")
    public String profilo() {
        return "profilo/profilo";
    }
}
