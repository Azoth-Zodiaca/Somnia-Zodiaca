package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController {

    @GetMapping("/shop")
    public String redirectShop() {
        return "redirect:/app/shop";
    }

    @GetMapping("/app/shop")
    public String shop() {
        return "app/shop";
    }
}
