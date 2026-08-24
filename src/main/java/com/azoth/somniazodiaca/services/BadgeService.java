package com.azoth.somniazodiaca.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azoth.somniazodiaca.entities.Badge;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.entities.UtenteBadge;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.enums.TipoCondizione;
import com.azoth.somniazodiaca.repositories.BadgeRepository;
import com.azoth.somniazodiaca.repositories.InterpretazioneRepository;
import com.azoth.somniazodiaca.repositories.LikePostRepository;
import com.azoth.somniazodiaca.repositories.PostRepository;
import com.azoth.somniazodiaca.repositories.SognoRepository;
import com.azoth.somniazodiaca.repositories.UtenteBadgeRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UtenteBadgeRepository utenteBadgeRepository;
    private final UtenteRepository utenteRepository;
    private final SognoRepository sognoRepository;
    private final InterpretazioneRepository interpretazioneRepository;
    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;

    public BadgeService(
            BadgeRepository badgeRepository,
            UtenteBadgeRepository utenteBadgeRepository,
            UtenteRepository utenteRepository,
            SognoRepository sognoRepository,
            InterpretazioneRepository interpretazioneRepository,
            PostRepository postRepository,
            LikePostRepository likePostRepository) {

        this.badgeRepository = badgeRepository;
        this.utenteBadgeRepository = utenteBadgeRepository;
        this.utenteRepository = utenteRepository;
        this.sognoRepository = sognoRepository;
        this.interpretazioneRepository = interpretazioneRepository;
        this.postRepository = postRepository;
        this.likePostRepository = likePostRepository;
    }

    @Transactional
    public void verificaBadge(String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        long utenteId = utente.getId();

        verifica(
                utente,
                TipoCondizione.NUMERO_SOGNI,
                sognoRepository.countByUtente_Id(utenteId));

        verifica(
                utente,
                TipoCondizione.NUMERO_INTERPRETAZIONI,
                interpretazioneRepository.countBySogno_Utente_Id(utenteId));

        verifica(
                utente,
                TipoCondizione.NUMERO_POST,
                postRepository.countByUtente_Id(utenteId));

        verifica(
                utente,
                TipoCondizione.LIKE_RICEVUTI,
                likePostRepository.countLikeRicevuti(utenteId));
                
        long giorniConsecutivi = utente.getGiorniConsecutivi() == null
                ? 0
                : utente.getGiorniConsecutivi();
                
        verifica(
                utente,
                TipoCondizione.GIORNI_CONSECUTIVI,
                giorniConsecutivi);
        
                if (utente.getRuolo() == Ruolo.PREMIUM) {
                    verifica(utente, TipoCondizione.UTENTE_PREMIUM, 1);
                }
            }
    
    private void verifica(
            Utente utente,
            TipoCondizione tipoCondizione,
            long progresso) {

        List<Badge> badgeDisponibili = badgeRepository
                .findAll()
                .stream()
                .filter(Badge::getAttivo)
                .filter(badge -> badge.getTipoCondizione() == tipoCondizione)
                .toList();

        for (Badge badge : badgeDisponibili) {
            Integer soglia = badge.getSoglia();

            if (soglia != null && progresso < soglia) {
                continue;
            }

            if (!utenteBadgeRepository.existsByUtente_IdAndBadge_Id(
                    utente.getId(),
                    badge.getId())) {

                utenteBadgeRepository.save(
                        UtenteBadge.builder()
                                .utente(utente)
                                .badge(badge)
                                .build());

                if (badge.getRicompensaQi() > 0) {
                    utente.setQi(
                            utente.getQi() + badge.getRicompensaQi());

                    utenteRepository.save(utente);
                }
            }
        }
    }
}