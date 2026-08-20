package com.azoth.somniazodiaca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WalletController {

    @GetMapping("/wallet")
    public String redirectWallet() {
        return "redirect:/app/wallet";
    }

    @GetMapping("/app/wallet")
    public String wallet() {
        return "app/wallet";
    }
}
