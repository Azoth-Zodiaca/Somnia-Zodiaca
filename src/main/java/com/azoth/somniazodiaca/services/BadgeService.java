package com.azoth.somniazodiaca.services;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azoth.somniazodiaca.entities.Badge;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.entities.UtenteBadge;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.enums.TipoCondizione;
import com.azoth.somniazodiaca.repositories.BadgeRepository;
import com.azoth.somniazodiaca.repositories.CommentoRepository;
import com.azoth.somniazodiaca.repositories.InterpretazioneRepository;
import com.azoth.somniazodiaca.repositories.LikePostRepository;
import com.azoth.somniazodiaca.repositories.PostRepository;
import com.azoth.somniazodiaca.repositories.SognoRepository;
import com.azoth.somniazodiaca.repositories.TemaNataleRepository;
import com.azoth.somniazodiaca.repositories.UtenteBadgeRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;
import com.azoth.somniazodiaca.dtos.BadgeViewDto;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UtenteBadgeRepository utenteBadgeRepository;
    private final UtenteRepository utenteRepository;
    private final SognoRepository sognoRepository;
    private final InterpretazioneRepository interpretazioneRepository;
    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;
        private final CommentoRepository commentoRepository;
        private final TemaNataleRepository temaNataleRepository;

    public BadgeService(
            BadgeRepository badgeRepository,
            UtenteBadgeRepository utenteBadgeRepository,
            UtenteRepository utenteRepository,
            SognoRepository sognoRepository,
            InterpretazioneRepository interpretazioneRepository,
            PostRepository postRepository,
            LikePostRepository likePostRepository,
            CommentoRepository commentoRepository,
            TemaNataleRepository temaNataleRepository) {

        this.badgeRepository = badgeRepository;
        this.utenteBadgeRepository = utenteBadgeRepository;
        this.utenteRepository = utenteRepository;
        this.sognoRepository = sognoRepository;
        this.interpretazioneRepository = interpretazioneRepository;
        this.postRepository = postRepository;
        this.likePostRepository = likePostRepository;
        this.commentoRepository = commentoRepository;
        this.temaNataleRepository = temaNataleRepository;
    }

    @Transactional(readOnly = true)
    public List<BadgeViewDto> getProgressi(String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        Map<Long, UtenteBadge> badgeOttenuti = utenteBadgeRepository
                .findByUtente_IdOrderByCreatedAtDesc(utente.getId())
                .stream()
                .collect(Collectors.toMap(
                        utenteBadge -> utenteBadge.getBadge().getId(),
                        Function.identity(),
                        (primo, secondo) -> primo));

        return badgeRepository.findAll()
                .stream()
                .filter(badge -> Boolean.TRUE.equals(badge.getAttivo()))
                .map(badge -> {
                    UtenteBadge utenteBadge = badgeOttenuti.get(badge.getId());
                    long progresso = calcolaProgresso(utente, badge);

                    return BadgeViewDto.builder()
                            .codice(badge.getCodice())
                            .nome(badge.getNome())
                            .descrizione(badge.getDescrizione())
                            .icona(badge.getIcona())
                            .sbloccato(utenteBadge != null)
                            .progresso(progresso)
                            .soglia(badge.getSoglia())
                            .ricompensaQi(badge.getRicompensaQi())
                            .ottenutoIl(utenteBadge == null
                                    ? null
                                    : utenteBadge.getCreatedAt())
                            .build();
                })
                .toList();
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

        verifica(
                utente,
                TipoCondizione.MAPPA_NATALE,
                temaNataleRepository.findByUtenteId(utenteId).isPresent() ? 1 : 0);

        verifica(
                utente,
                TipoCondizione.NUMERO_COMMENTI,
                commentoRepository.countByUtente_Id(utenteId));

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

    private long calcolaProgresso(
            Utente utente,
            Badge badge) {

        Long utenteId = utente.getId();

        return switch (badge.getTipoCondizione()) {
            case NUMERO_SOGNI ->
                sognoRepository.countByUtente_Id(utenteId);

            case NUMERO_INTERPRETAZIONI ->
                interpretazioneRepository
                        .countBySogno_Utente_Id(utenteId);

            case NUMERO_POST ->
                postRepository.countByUtente_Id(utenteId);

            case LIKE_RICEVUTI ->
                likePostRepository.countLikeRicevuti(utenteId);

            case GIORNI_CONSECUTIVI ->
                utente.getGiorniConsecutivi() == null
                        ? 0
                        : utente.getGiorniConsecutivi();

            case UTENTE_PREMIUM ->
                utente.getRuolo() == Ruolo.PREMIUM ? 1 : 0;

            case MAPPA_NATALE ->
                temaNataleRepository.findByUtenteId(utenteId).isPresent() ? 1 : 0;

            case NUMERO_COMMENTI ->
                commentoRepository.countByUtente_Id(utenteId);
        };
    }

}