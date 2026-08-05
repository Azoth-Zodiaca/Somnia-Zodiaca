package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OracoloDiarioController {

    @GetMapping("/oracolo/diario")
    public String oracoloDiario() {
        return "oracolo/oracolo-diario";
    }
}
