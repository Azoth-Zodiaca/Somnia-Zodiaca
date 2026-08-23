package com.azoth.somniazodiaca.config;

import org.springframework.context.annotation.Configuration;

import com.azoth.somniazodiaca.entities.Badge;
import com.azoth.somniazodiaca.enums.TipoCondizione;
import com.azoth.somniazodiaca.repositories.BadgeRepository;

@Configuration
public class BadgeInitializer {

    private final BadgeRepository badgeRepository;

    public BadgeInitializer(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    public void inizializza() {
        creaBadge(
                "PRIMO_SOGNO",
                "Primo Sogno",
                "Salva il tuo primo sogno.",
                "🌙",
                TipoCondizione.NUMERO_SOGNI,
                1,
                10);

        creaBadge(
                "DIARIO_PIENO",
                "Diario Pieno",
                "Salva 25 sogni.",
                "📖",
                TipoCondizione.NUMERO_SOGNI,
                25,
                50);

        creaBadge(
                "STELLA_NASCENTE",
                "Stella Nascente",
                "Ricevi 100 like sui tuoi post.",
                "⭐",
                TipoCondizione.LIKE_RICEVUTI,
                100,
                75);

        creaBadge(
                "ORACOLO",
                "Oracolo",
                "Ottieni 50 interpretazioni.",
                "🔮",
                TipoCondizione.NUMERO_INTERPRETAZIONI,
                50,
                100);

        creaBadge(
                "COLLEZIONISTA",
                "Collezionista",
                "Possiedi 25 cosmetici.",
                "💎",
                TipoCondizione.NUMERO_COSMETICI,
                25,
                100);

        creaBadge(
                "COSTANZA",
                "Costanza",
                "Raggiungi una serie di 30 giorni consecutivi.",
                "🔥",
                TipoCondizione.GIORNI_CONSECUTIVI,
                30,
                150);

        creaBadge(
                "AMBASCIATORE",
                "Ambasciatore",
                "Pubblica 10 post.",
                "💬",
                TipoCondizione.NUMERO_POST,
                10,
                75);

        creaBadge(
                "PREMIUM",
                "Premium",
                "Attiva un abbonamento Premium.",
                "👑",
                TipoCondizione.UTENTE_PREMIUM,
                null,
                0);

        System.out.println("Badge verificati.");
    }

    private void creaBadge(
            String codice,
            String nome,
            String descrizione,
            String icona,
            TipoCondizione tipoCondizione,
            Integer soglia,
            Integer ricompensaQi) {

        if (badgeRepository.findByCodice(codice).isEmpty()) {
            Badge badge = Badge.builder()
                    .codice(codice)
                    .nome(nome)
                    .descrizione(descrizione)
                    .icona(icona)
                    .tipoCondizione(tipoCondizione)
                    .soglia(soglia)
                    .ricompensaQi(ricompensaQi)
                    .attivo(true)
                    .build();

            badgeRepository.save(badge);
        }
    }
}