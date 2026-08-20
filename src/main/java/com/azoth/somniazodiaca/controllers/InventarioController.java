package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventarioController {

    @GetMapping("/inventario")
    public String redirectInventario() {
        return "redirect:/app/inventario";
    }

    @GetMapping("/app/inventario")
    public String inventario() {
        return "app/inventario";
    }
}
