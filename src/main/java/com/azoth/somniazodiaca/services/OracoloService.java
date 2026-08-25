package com.azoth.somniazodiaca.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azoth.somniazodiaca.dtos.records.SalvaInterpretazioneRequest;
import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.StileEnum;
import com.azoth.somniazodiaca.enums.UmoreEnum;
import com.azoth.somniazodiaca.repositories.InterpretazioneRepository;
import com.azoth.somniazodiaca.repositories.SognoRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

@Service
public class OracoloService {

    private static final int COSTO_INTERPRETAZIONE = 20;
    private static final long DURATA_INTERPRETAZIONE_ORE = 48;

    private final UtenteRepository utenteRepository;
    private final SognoRepository sognoRepository;
    private final InterpretazioneRepository interpretazioneRepository;
    private final BadgeService badgeService;

    public OracoloService(
            UtenteRepository utenteRepository,
            SognoRepository sognoRepository,
            InterpretazioneRepository interpretazioneRepository,
            BadgeService badgeService) {

        this.utenteRepository = utenteRepository;
        this.sognoRepository = sognoRepository;
        this.interpretazioneRepository = interpretazioneRepository;
        this.badgeService = badgeService;
    }

    public void verificaQiDisponibili(String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (utente.getQi() < COSTO_INTERPRETAZIONE) {
            throw new IllegalStateException("QI insufficienti");
        }
    }

    @Transactional
    public Long salvaInterpretazione(
            String username,
            SalvaInterpretazioneRequest richiesta) {

        if (richiesta.testoSogno() == null
                || richiesta.testoSogno().isBlank()
                || richiesta.interpretazione() == null
                || richiesta.interpretazione().isBlank()) {
            throw new IllegalArgumentException("Dati incompleti");
        }

        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (utente.getQi() < COSTO_INTERPRETAZIONE) {
            throw new IllegalStateException("QI insufficienti");
        }

        Sogno sogno = Sogno.builder()
                .utente(utente)
                .testo(richiesta.testoSogno())
                .build();

        sognoRepository.save(sogno);

        Interpretazione interpretazione = Interpretazione.builder()
                .sogno(sogno)
                .prompt(richiesta.prompt())
                .testo(richiesta.interpretazione())
                .umore(parseEnum(richiesta.umore(), UmoreEnum.class))
                .stile(parseEnum(richiesta.stile(), StileEnum.class))
                .scadenzaCache(LocalDateTime.now().plusHours(DURATA_INTERPRETAZIONE_ORE))
                .build();

        Interpretazione salvata = interpretazioneRepository.save(interpretazione);

        utente.setQi(utente.getQi() - COSTO_INTERPRETAZIONE);
        utenteRepository.save(utente);

        badgeService.verificaBadge(username);

        return salvata.getId();
    }

    private <T extends Enum<T>> T parseEnum(String value, Class<T> enumType) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Umore e stile sono obbligatori");
        }

        try {
            return Enum.valueOf(enumType, value.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Valore non valido: " + value, exception);
        }
    }

    @Transactional
    public void rendiPermanente(String username, Long interpretazioneId) {
        Interpretazione interpretazione = interpretazioneRepository
                .findById(interpretazioneId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Interpretazione non trovata"));

        if (!interpretazione.getSogno().getUtente().getUsername().equals(username)) {
            throw new IllegalArgumentException("Interpretazione non autorizzata");
        }

        if (interpretazione.getScadenzaCache() == null) {
            return;
        }

        Utente utente = interpretazione.getSogno().getUtente();

        if (utente.getQi() < COSTO_INTERPRETAZIONE) {
            throw new IllegalStateException("QI insufficienti");
        }

        interpretazione.setScadenzaCache(null);
        utente.setQi(utente.getQi() - COSTO_INTERPRETAZIONE);
        interpretazioneRepository.save(interpretazione);
        utenteRepository.save(utente);
    }

    @Transactional
    public void cancellaInterpretazione(String username, Long interpretazioneId) {
        Interpretazione interpretazione = interpretazioneRepository
                .findById(interpretazioneId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Interpretazione non trovata"));

        if (!interpretazione.getSogno().getUtente().getUsername().equals(username)) {
            throw new IllegalArgumentException("Interpretazione non autorizzata");
        }

        interpretazione.setScadenzaCache(LocalDateTime.now());
        interpretazioneRepository.save(interpretazione);
    }
}
