package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProgressiController {

    @GetMapping("/progressi")
    public String progressi() {
        return "progressi/progressi";
    }
}
