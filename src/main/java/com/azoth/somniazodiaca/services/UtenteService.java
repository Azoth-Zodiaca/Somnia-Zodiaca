package com.azoth.somniazodiaca.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.UtenteConverter;
import com.azoth.somniazodiaca.dtos.CreazioneUtenteDto;
import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.entities.TemaNatale;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

@Service
public class UtenteService extends GenericService<Long, Utente, UtenteDetail, UtenteConverter, UtenteRepository> {

    public UtenteService(UtenteRepository repository, UtenteConverter converter) {
        super(repository, converter);
    }

    public Optional<UtenteDetail> findByUsername(String username) {
        return getRepository().findByUsername(username).map(getConverter()::fromEToD);
    }

    public Optional<UtenteDetail> findByEmail(String email) {
        return getRepository().findByEmail(email).map(getConverter()::fromEToD);
    }

    public Optional<UtenteDetail> authenticate(String usernameOrEmail, String password) {
        return getRepository()
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .filter(u -> u.getPassword() != null && u.getPassword().equals(password))
                .map(getConverter()::fromEToD);
    }

    public UtenteDetail register(CreazioneUtenteDto dto) {
        Utente utente = Utente.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .ruolo(dto.getRuolo())
                .qi(250)
                .dataRegistrazione(LocalDateTime.now())
                .ultimoAccesso(LocalDateTime.now())
                .build();

        TemaNatale temaNatale = TemaNatale.builder()
                .utente(utente)
                .dataNascita(dto.getDataNascita())
                .oraNascita(dto.getOraNascita())
                .luogoNascita(dto.getLuogoNascita())
                .dataCreazione(LocalDateTime.now())
                .build();

        utente.setTemaNatale(temaNatale);
        getRepository().save(utente);
        return getConverter().fromEToD(utente);
    }

    public List<UtenteDetail> getAllUsers() {
        return getAll();
    }

    public Optional<UtenteDetail> findById(Long id) {
        return getRepository().findById(id).map(getConverter()::fromEToD);
    }
}
