package com.azoth.somniazodiaca.controllers;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.azoth.somniazodiaca.dtos.BadgeViewDto;
import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.services.BadgeService;
import com.azoth.somniazodiaca.services.UtenteService;
import com.azoth.somniazodiaca.services.PostService;
import com.azoth.somniazodiaca.repositories.LikePostRepository;
import com.azoth.somniazodiaca.repositories.SognoRepository;
import com.azoth.somniazodiaca.services.ElementoService;
import com.azoth.somniazodiaca.services.PianetaService;
import com.azoth.somniazodiaca.services.MetalloService;
@Controller
public class ProfiloController {

    private final UtenteService utenteService;
    private final BadgeService badgeService;
    private final PostService postService;
    private final LikePostRepository likePostRepository;
    private final SognoRepository sognoRepository;
    private final ElementoService elementoService;
    private final PianetaService pianetaService;
    private final MetalloService metalloService;

    public ProfiloController(
            UtenteService utenteService,
            BadgeService badgeService,
            PostService postService,
            LikePostRepository likePostRepository,
            SognoRepository sognoRepository,
            ElementoService elementoService,
            PianetaService pianetaService,
            MetalloService metalloService) {

        this.utenteService = utenteService;
        this.badgeService = badgeService;
        this.postService = postService;
        this.likePostRepository = likePostRepository;
        this.sognoRepository = sognoRepository;
        this.elementoService = elementoService;
        this.pianetaService = pianetaService;
        this.metalloService = metalloService;
    }

    @GetMapping("/profilo")
    public String redirectProfilo() {
        return "redirect:/app/profilo";
    }

    @GetMapping("/app/profilo")
    public String profilo(Authentication authentication, Model model) {
        UtenteDetail utente = utenteService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Utente autenticato non trovato"));

        long numeroPost = postService.findByUtenteId(utente.getId()).size();
        long numeroLike = likePostRepository.countLikeRicevuti(utente.getId());
        long numeroSogni = sognoRepository.countByUtente_Id(utente.getId());

        model.addAttribute("numeroPost", numeroPost);
        model.addAttribute("numeroLike", numeroLike);
        model.addAttribute("numeroSogni", numeroSogni);
        model.addAttribute("postsProfilo", postService.findByUtenteId(utente.getId()));
        model.addAttribute("currentUsername", authentication.getName());
        model.addAttribute("username", utente.getUsername());
        model.addAttribute("ruolo", utente.getRuolo());
        model.addAttribute("saldoQI", utente.getQi());
        model.addAttribute("profiloSegnoZodiacale", utente.getSegnoZodiacale());
        model.addAttribute("profiloAscendente", utente.getAscendente());
        model.addAttribute("dataRegistrazione", utente.getDataRegistrazione());
        model.addAttribute("ultimoAccesso", utente.getUltimoAccesso());
        model.addAttribute("profiloColore", utente.getProfiloColore());
        model.addAttribute("avatarPath", utente.getAvatarPath());
        model.addAttribute("bannerPath", utente.getBannerPath());

        List<BadgeViewDto> badges = badgeService
                .getProgressi(authentication.getName());
            
        long badgeSbloccati = badges.stream()
                .filter(BadgeViewDto::sbloccato)
                .count();
            
        model.addAttribute("badges", badges);
        model.addAttribute("badgeSbloccati", badgeSbloccati);
        model.addAttribute("totaleBadge", badges.size());
        
        if (utente.getSegnoZodiacale() != null) {
            var segno = utente.getSegnoZodiacale();
        
            if (segno.getElementoId() != null) {
                model.addAttribute(
                        "elementoProfilo",
                        elementoService.getById(segno.getElementoId()));
            }
        
            if (segno.getPianetaId() != null) {
                model.addAttribute(
                        "pianetaProfilo",
                        pianetaService.getById(segno.getPianetaId()));
            }
        
            if (segno.getMetalloId() != null) {
                model.addAttribute(
                        "metalloProfilo",
                        metalloService.getById(segno.getMetalloId()));
            }
        }

        return "app/profilo";
    }

    @PostMapping(value = "/app/profilo/immagini", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String aggiornaImmagini(
            Authentication authentication,
            @RequestParam(required = false) MultipartFile avatar,
            @RequestParam(required = false) MultipartFile banner) {

        utenteService.aggiornaImmagini(
                authentication.getName(),
                avatar,
                banner);

        return "redirect:/app/profilo";
    }

}
