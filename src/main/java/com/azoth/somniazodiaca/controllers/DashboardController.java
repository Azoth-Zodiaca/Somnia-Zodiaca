package com.azoth.somniazodiaca.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.azoth.somniazodiaca.config.AppDomainProperties;
import com.azoth.somniazodiaca.services.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService,
            AppDomainProperties appDomainProperties) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String redirectDashboard() {
        return "redirect:/app/dashboard";
    }

    @GetMapping("/app/dashboard")
    public String dashboard(
            Authentication authentication,
            Model model) {

        DashboardService.DashboardData dashboard = dashboardService.getDashboardData(authentication.getName());

        model.addAttribute("saldoQi", dashboard.saldoQi());
        model.addAttribute("numeroSogni", dashboard.numeroSogni());
        model.addAttribute("numeroLike", dashboard.numeroLike());
        model.addAttribute("streak", dashboard.streak());
        model.addAttribute(
                "sogniInScadenza",
                dashboard.sogniInScadenza());

        return "app/dashboard";
    }
}