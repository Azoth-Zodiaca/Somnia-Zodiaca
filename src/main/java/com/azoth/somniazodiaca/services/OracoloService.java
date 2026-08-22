package com.azoth.somniazodiaca.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azoth.somniazodiaca.dtos.records.SalvaInterpretazioneRequest;
import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.InterpretazioneEnum;
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

    public OracoloService(
            UtenteRepository utenteRepository,
            SognoRepository sognoRepository,
            InterpretazioneRepository interpretazioneRepository) {

        this.utenteRepository = utenteRepository;
        this.sognoRepository = sognoRepository;
        this.interpretazioneRepository = interpretazioneRepository;
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
                .interpretazioneEnum(InterpretazioneEnum.JUNGIANA)
                .scadenzaCache(LocalDateTime.now().plusHours(48))
                .build();

        Interpretazione salvata = interpretazioneRepository.save(interpretazione);

        utente.setQi(utente.getQi() - COSTO_INTERPRETAZIONE);
        utenteRepository.save(utente);

        return salvata.getId();
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

        interpretazione.setScadenzaCache(null);
        interpretazioneRepository.save(interpretazione);
    }
}
