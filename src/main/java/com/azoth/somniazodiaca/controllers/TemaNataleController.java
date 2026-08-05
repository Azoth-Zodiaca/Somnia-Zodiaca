package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemaNataleController {

    @GetMapping("/tema-natale")
    public String temaNatale() {
        return "tema-natale/tema-natale";
    }
}
