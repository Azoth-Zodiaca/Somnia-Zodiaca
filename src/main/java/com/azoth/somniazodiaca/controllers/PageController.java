package com.azoth.somniazodiaca.controllers;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azoth.somniazodiaca.dtos.AggiuntaInventarioCosmeticoDto;
import com.azoth.somniazodiaca.dtos.AggiuntaSognoDto;
import com.azoth.somniazodiaca.dtos.CosmeticoDto;
import com.azoth.somniazodiaca.dtos.PostDto;
import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.services.CosmeticoService;
import com.azoth.somniazodiaca.services.InventarioCosmeticoService;
import com.azoth.somniazodiaca.services.PostService;
import com.azoth.somniazodiaca.services.SognoService;
import com.azoth.somniazodiaca.services.TemaNataleService;
import com.azoth.somniazodiaca.services.UtenteService;

@Controller
public class PageController {

    private final UtenteService utenteService;
    private final InventarioCosmeticoService inventarioCosmeticoService;
    private final TemaNataleService temaNataleService;
    private final PostService postService;
    private final CosmeticoService cosmeticoService;
    private final SognoService sognoService;

    public PageController(
            UtenteService utenteService,
            InventarioCosmeticoService inventarioCosmeticoService,
            TemaNataleService temaNataleService,
            PostService postService,
            CosmeticoService cosmeticoService,
            SognoService sognoService) {
        this.utenteService = utenteService;
        this.inventarioCosmeticoService = inventarioCosmeticoService;
        this.temaNataleService = temaNataleService;
        this.postService = postService;
        this.cosmeticoService = cosmeticoService;
        this.sognoService = sognoService;
    }

    private Optional<Long> getCurrentUserId(HttpSession session) {
        Object value = session.getAttribute("currentUserId");
        if (value instanceof Long) {
            return Optional.of((Long) value);
        }
        if (value instanceof Integer) {
            return Optional.of(((Integer) value).longValue());
        }
        return Optional.empty();
    }

    private void bindUtente(Model model, HttpSession session) {
        getCurrentUserId(session)
                .flatMap(utenteService::findById)
                .ifPresent(utente -> model.addAttribute("utente", utente));
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        bindUtente(model, session);
        return "index";
    }

    @GetMapping("/oracolo")
    public String oracolo(Model model, HttpSession session) {
        bindUtente(model, session);
        return "oracolo";
    }

    @PostMapping("/oracolo")
    public String submitOracolo(
            @RequestParam String testo,
            Model model,
            HttpSession session) {
        Optional<Long> currentUserId = getCurrentUserId(session);
        if (currentUserId.isEmpty()) {
            model.addAttribute("oracoloError", "Devi effettuare il login per inviare un sogno.");
            return "login";
        }

        AggiuntaSognoDto sogno = AggiuntaSognoDto.builder()
                .utenteId(currentUserId.get())
                .testo(testo)
                .build();

        sognoService.save(sogno);
        return "redirect:/oracolo/diario";
    }

    @GetMapping("/oracolo/diario")
    public String oracoloDiario(Model model, HttpSession session) {
        getCurrentUserId(session).ifPresent(userId -> {
            model.addAttribute("sogni", sognoService.findByUtenteId(userId));
            bindUtente(model, session);
        });
        return "oracolo-diario";
    }

    @GetMapping("/social")
    public String social(Model model) {
        List<PostDto> posts = postService.getAll();
        model.addAttribute("posts", posts);
        return "social";
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        List<CosmeticoDto> cosmetici = cosmeticoService.getAll();
        model.addAttribute("cosmetici", cosmetici);
        return "shop";
    }

    @GetMapping("/inventario")
    public String inventario(Model model, HttpSession session) {
        Optional<Long> currentUserId = getCurrentUserId(session);
        if (currentUserId.isPresent()) {
            List<AggiuntaInventarioCosmeticoDto> inventario = inventarioCosmeticoService.findByUtenteId(currentUserId.get());
            model.addAttribute("inventario", inventario);
            bindUtente(model, session);
        }
        return "inventario";
    }

    @GetMapping("/tema-natale")
    public String temaNatale(Model model, HttpSession session) {
        Optional<Long> currentUserId = getCurrentUserId(session);
        if (currentUserId.isPresent()) {
            Optional<TemaNataleDto> tema = temaNataleService.findByUtenteId(currentUserId.get());
            tema.ifPresent(t -> model.addAttribute("temaNatale", t));
            bindUtente(model, session);
        }
        return "tema-natale";
    }

    @GetMapping("/profilo")
    public String profilo(Model model, HttpSession session) {
        bindUtente(model, session);
        return "profilo";
    }

    @GetMapping("/wallet")
    public String wallet(Model model, HttpSession session) {
        bindUtente(model, session);
        return "wallet";
    }

    @GetMapping("/impostazioni")
    public String impostazioni(Model model, HttpSession session) {
        bindUtente(model, session);
        return "impostazioni";
    }

    @GetMapping("/progressi")
    public String progressi(Model model, HttpSession session) {
        bindUtente(model, session);
        return "progressi";
    }
}
