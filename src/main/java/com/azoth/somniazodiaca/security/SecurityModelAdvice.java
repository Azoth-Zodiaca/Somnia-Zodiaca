package com.azoth.somniazodiaca.security;

import java.time.LocalDate;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.services.UtenteService;

/**
 * Espone alle view un oggetto semplice chiamato currentUser.
 *
 * Spring passa qui l'oggetto Authentication della richiesta corrente: è il
 * contenitore
 * che rappresenta l'utente loggato, i suoi ruoli e il suo stato di
 * autenticazione.
 * Da quel contenitore estraiamo solo i dati minimi utili alle pagine HTML.
 */
@ControllerAdvice
public class SecurityModelAdvice {

    public final UtenteService utenteService;

    public SecurityModelAdvice(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    /**
     * Costruisce il modello CurrentUserView da Authentication.
     *
     * Se non c'è un utente autenticato, oppure Spring sta usando l'utente anonimo,
     * restituiamo un oggetto vuoto con tutti i flag a false.
     *
     * Questo metodo non concede permessi: prepara soltanto dati da leggere nelle
     * view.
     */
    @ModelAttribute("currentUser")
    public CurrentUserView currentUser(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            // Nessun login valido: la view riceve valori neutri.
            return new CurrentUserView("", false, false);
        }

        // Qui leggiamo i ruoli reali dell'utente dall'oggetto Authentication.
        // Spring li conserva come GrantedAuthority, ad esempio ROLE_ADMIN e
        // ROLE_OPERATORE.
        boolean admin = hasAuthority(authentication, "ROLE_ADMIN");
        boolean operatore = hasAuthority(authentication, "ROLE_OPERATORE");
        // Dal contenitore completo estraiamo solo quello che serve alle pagine.
        return new CurrentUserView(authentication.getName(), admin, operatore);
    }

    /**
     * Controlla se l'utente possiede una specifica authority Spring Security.
     *
     * Le authorities sono le etichette tecniche con cui Spring rappresenta i ruoli.
     * Per esempio, il ruolo ADMIN viene visto internamente come ROLE_ADMIN.
     */
    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority::equals);
    }

    private String abbrevia(String segno) {
        return switch (segno) {
            case "ARIETE" -> "Ari";
            case "TORO" -> "Tau";
            case "GEMELLI" -> "Gem";
            case "CANCRO" -> "Can";
            case "LEONE" -> "Leo";
            case "VERGINE" -> "Vir";
            case "BILANCIA" -> "Lib";
            case "SCORPIONE" -> "Sco";
            case "SAGITTARIO" -> "Sag";
            case "CAPRICORNO" -> "Cap";
            case "ACQUARIO" -> "Aqu";
            case "PESCI" -> "Pis";
            default -> segno;
        };
    }

    private String simboloSegno(String segno) {
        return switch (segno) {
            case "ARIETE" -> "\u2648\uFE0E";
            case "TORO" -> "\u2649\uFE0E";
            case "GEMELLI" -> "\u264A\uFE0E";
            case "CANCRO" -> "\u264B\uFE0E";
            case "LEONE" -> "\u264C\uFE0E";
            case "VERGINE" -> "\u264D\uFE0E";
            case "BILANCIA" -> "\u264E\uFE0E";
            case "SCORPIONE" -> "\u264F\uFE0E";
            case "SAGITTARIO" -> "\u2650\uFE0E";
            case "CAPRICORNO" -> "\u2651\uFE0E";
            case "ACQUARIO" -> "\u2652\uFE0E";
            case "PESCI" -> "\u2653\uFE0E";
            default -> "";
        };
    }

    private String classeColore(String colore) {
        return switch (colore) {
            case "#F97316" -> "profile-color-orange";
            case "#E11D48" -> "profile-color-red";
            case "#16A34A" -> "profile-color-green";
            case "#2563EB" -> "profile-color-blue";
            case "#9333EA" -> "profile-color-purple";
            default -> "profile-color-orange";
        };
    }

    @ModelAttribute
    public void sidebarAttributes(
            Authentication authentication,
            Model model) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            model.addAttribute("username", "");
            model.addAttribute("saldoQi", 0);
            model.addAttribute("ricompensaRiscossa", false);
            model.addAttribute("segnoZodiacale", null);
            model.addAttribute("segnoZodiacaleAbbreviato", null);
            model.addAttribute("segnoZodiacaleSimbolo", null);
            model.addAttribute("ascendenteSimbolo", null);
            model.addAttribute("avatarPath", null);
            model.addAttribute("profiloColore", "#F97316");
            model.addAttribute(
                    "profiloColoreClasse",
                    "profile-color-orange");

            return;
        }

        UtenteDetail utente = utenteService
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Utente non trovato"));

        model.addAttribute("username", utente.getUsername());
        model.addAttribute("saldoQi", utente.getQi());
        model.addAttribute(
            "ricompensaRiscossa",
            LocalDate.now().equals(utente.getUltimaRicompensaGiornaliera()));
        model.addAttribute("avatarPath", utente.getAvatarPath());

        String colore = utente.getProfiloColore() != null
                ? utente.getProfiloColore()
                : "#F97316";

        model.addAttribute("profiloColore", colore);
        model.addAttribute(
                "profiloColoreClasse",
                classeColore(colore));

        if (utente.getSegnoZodiacale() == null) {
            model.addAttribute("segnoZodiacale", null);
            model.addAttribute("segnoZodiacaleAbbreviato", null);
            model.addAttribute("segnoZodiacaleSimbolo", null);
        } else {
            String segno = utente.getSegnoZodiacale()
                    .getSegnoZodiacale()
                    .name();

            model.addAttribute("segnoZodiacale", segno);
            model.addAttribute(
                    "segnoZodiacaleAbbreviato",
                    abbrevia(segno));
            model.addAttribute(
                    "segnoZodiacaleSimbolo",
                    simboloSegno(segno));
        }

        if (utente.getAscendente() == null) {
            model.addAttribute("ascendenteSimbolo", null);
        } else {
            String ascendente = utente.getAscendente()
                    .getSegnoZodiacale()
                    .name();

            model.addAttribute(
                    "ascendenteSimbolo",
                    simboloSegno(ascendente));
        }
    }
}
