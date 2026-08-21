package com.azoth.somniazodiaca.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    @ModelAttribute("saldoQi")
    public Integer saldoQi(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }

        return utenteService.findByUsername(authentication.getName())
                .map(UtenteDetail::getQi)
                .orElse(0);
    }
}
