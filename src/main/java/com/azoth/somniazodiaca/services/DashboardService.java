package com.azoth.somniazodiaca.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.RichiestaInterpretazioneDto;
import com.azoth.somniazodiaca.dtos.UtenteDetail;

@Service
public class DashboardService {

        private final UtenteService utenteService;
        private final SognoService sognoService;
        private final PostService postService;
        private final InterpretazioneService interpretazioneService;

        public DashboardService(
                        UtenteService utenteService,
                        SognoService sognoService,
                        PostService postService,
                        InterpretazioneService interpretazioneService) {

                this.utenteService = utenteService;
                this.sognoService = sognoService;
                this.postService = postService;
                this.interpretazioneService = interpretazioneService;
        }

        public DashboardData getDashboardData(String username) {
                UtenteDetail utente = utenteService
                                .findByUsername(username)
                                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

                return new DashboardData(
                                utente.getQi(),
                                sognoService.countSogniDisponibiliByUtenteId(utente.getId(),
                                                java.time.LocalDateTime.now()),
                                postService.countLikeRicevuti(utente.getId()),
                                utente.getGiorniRicompensaGiornaliera() == null
                                                ? 0
                                                : utente.getGiorniRicompensaGiornaliera(),
                                interpretazioneService.findProssimeInScadenza(utente.getId()));
        }

        public record DashboardData(
                        Integer saldoQi,
                        long numeroSogni,
                        long numeroLike,
                        int streak,
                        List<RichiestaInterpretazioneDto> sogniInScadenza) {
        }
}