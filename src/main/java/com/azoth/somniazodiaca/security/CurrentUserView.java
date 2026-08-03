package com.azoth.somniazodiaca.security;

/**
 * Modello esposto alle view Thymeleaf.
 *
 * Questa classe esiste perché nelle pagine HTML non serve l'oggetto di sicurezza
 * completo di Spring. Con "oggetto di sicurezza completo" si intende l'istanza
 * di {@link org.springframework.security.core.Authentication} che Spring mette a
 * disposizione durante la richiesta HTTP: dentro quell'oggetto ci sono nome utente,
 * ruoli, stato di autenticazione e altre informazioni tecniche che servono al backend.
 *
 * Alle view, però, non serve tutto questo dettaglio. Per mostrare o nascondere pulsanti
 * e link bastano poche informazioni semplici:
 * - il nome dell'utente da mostrare nell'interfaccia;
 * - un flag per sapere se l'utente è ADMIN;
 * - un flag per sapere se l'utente è OPERATORE.
 *
 * Il record non decide i permessi reali: serve solo per abilitare o nascondere
 * pulsanti e link nell'interfaccia. La vera protezione resta nel backend,
 * dentro SecurityConfig e dentro gli eventuali @PreAuthorize dei service.
 *
 * @param username nome mostrato nella barra utente; può essere vuoto quando non c'è un utente autenticato
 * @param admin true se l'utente ha il ruolo ADMIN
 * @param operatore true se l'utente ha il ruolo OPERATORE
 */
public record CurrentUserView(String username, boolean admin, boolean operatore) {
}
