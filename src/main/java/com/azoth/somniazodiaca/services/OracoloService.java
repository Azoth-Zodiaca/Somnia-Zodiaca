package com.azoth.somniazodiaca.services;

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
    public void salvaInterpretazione(
            String username,
            SalvaInterpretazioneRequest richiesta) {

        if (richiesta.testoSogno() == null
                || richiesta.testoSogno().isBlank()
                || richiesta.interpretazione() == null
                || richiesta.interpretazione().isBlank()) {
            throw new IllegalArgumentException("Dati incompleti");
        }

        Utente utente = utenteRepository
                .findByUsername(username)
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
                .build();

        interpretazioneRepository.save(interpretazione);

        utente.setQi(utente.getQi() - COSTO_INTERPRETAZIONE);
        utenteRepository.save(utente);
    }
}
