package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.azoth.somniazodiaca.services.GeminiService;

@Controller
public class OracoloController {
    
    private final GeminiService geminiService;

    public OracoloController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }
    
    @GetMapping("/oracolo")
    public String redirectOracolo() {
        return "redirect:/app/oracolo";
    }

    @GetMapping("/app/oracolo")
    public String oracolo() {
        return "app/oracolo";
    }

    @PostMapping("/test")
    @ResponseBody
    public String test(@RequestBody String dream) {

        return geminiService.askGemini(
                "Interpreta questo sogno in modo simbolico: " + dream);
    }
}
