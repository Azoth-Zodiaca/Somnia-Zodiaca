package com.azoth.somniazodiaca.services;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azoth.somniazodiaca.dtos.records.SalvaInterpretazioneRequest;
import com.azoth.somniazodiaca.config.AppDomainProperties;
import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.StileEnum;
import com.azoth.somniazodiaca.enums.UmoreEnum;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.repositories.InterpretazioneRepository;
import com.azoth.somniazodiaca.repositories.SognoRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

@Service
public class OracoloService {

    private final UtenteRepository utenteRepository;
    private final SognoRepository sognoRepository;
    private final InterpretazioneRepository interpretazioneRepository;
    private final BadgeService badgeService;
    private final AppDomainProperties appDomainProperties;

    public OracoloService(
            UtenteRepository utenteRepository,
            SognoRepository sognoRepository,
            InterpretazioneRepository interpretazioneRepository,
            BadgeService badgeService,
            AppDomainProperties appDomainProperties) {

        this.utenteRepository = utenteRepository;
        this.sognoRepository = sognoRepository;
        this.interpretazioneRepository = interpretazioneRepository;
        this.badgeService = badgeService;
        this.appDomainProperties = appDomainProperties;
    }

    public void verificaQiDisponibili(String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (utente.getQi() < appDomainProperties.getOracolo().getCostoInterpretazione()) {
            throw new IllegalStateException("QI insufficienti");
        }
    }

    public void verificaAccessoInterpretazione(String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (utente.getRuolo() == Ruolo.BASE
                && contaInterpretazioniSettimana(utente)
                        >= appDomainProperties.getPremium().getInterpretazioniFreeSettimana()) {
            throw new IllegalStateException("Hai raggiunto il limite settimanale di interpretazioni");
        }

        if (utente.getQi() < appDomainProperties.getOracolo().getCostoInterpretazione()) {
            throw new IllegalStateException("QI insufficienti");
        }
    }

    public int interpretazioniResidue(String username) {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (utente.getRuolo() != Ruolo.BASE) {
            return -1;
        }

        int limite = appDomainProperties.getPremium().getInterpretazioniFreeSettimana();
        return Math.max(0, limite - (int) contaInterpretazioniSettimana(utente));
    }

    private long contaInterpretazioniSettimana(Utente utente) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime inizioSettimana = LocalDateTime.now(zoneId)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .toLocalDate()
                .atStartOfDay();

        return interpretazioneRepository
                .countBySogno_Utente_IdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                        utente.getId(), inizioSettimana, inizioSettimana.plusWeeks(1));
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

        verificaAccessoInterpretazione(username);

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
                .scadenzaCache(LocalDateTime.now().plusHours(
                    appDomainProperties.getOracolo().getDurataCacheOre()))
                .build();

        Interpretazione salvata = interpretazioneRepository.save(interpretazione);

        utente.setQi(utente.getQi() - appDomainProperties.getOracolo().getCostoInterpretazione());
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

        if (utente.getQi() < appDomainProperties.getOracolo().getCostoPermanenza()) {
            throw new IllegalStateException("QI insufficienti");
        }

        interpretazione.setScadenzaCache(null);
        utente.setQi(utente.getQi() - appDomainProperties.getOracolo().getCostoPermanenza());
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
