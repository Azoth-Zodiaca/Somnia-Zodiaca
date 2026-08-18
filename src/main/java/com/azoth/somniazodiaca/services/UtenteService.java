package com.azoth.somniazodiaca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.UtenteConverter;
import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.dtos.records.Registrazione;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.exceptions.EmailAlreadyExistsException;
import com.azoth.somniazodiaca.exceptions.UsernameAlreadyExistsException;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

import jakarta.transaction.Transactional;

@Service
public class UtenteService extends GenericService<Long, Utente, UtenteDetail, UtenteConverter, UtenteRepository> {

    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository repository, UtenteConverter converter, PasswordEncoder passwordEncoder) {
        super(repository, converter);
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UtenteDetail> findByUsername(String username) {
        return getRepository().findByUsername(username).map(getConverter()::fromEToD);
    }

    public Optional<UtenteDetail> findByEmail(String email) {
        return getRepository().findByEmail(email).map(getConverter()::fromEToD);
    }

    // public Optional<UtenteDetail> authenticate(String usernameOrEmail, String
    // password) {
    // return getRepository()
    // .findByUsernameOrEmail(usernameOrEmail)
    // .filter(u -> u.getPassword() != null && u.getPassword().equals(password))
    // .map(getConverter()::fromEToD);
    // }

    // TemaNatale temaNatale = TemaNatale.builder()
    // .utente(utente)
    // .dataNascita(dto.getDataNascita())
    // .oraNascita(dto.getOraNascita())
    // .luogoNascita(dto.getLuogoNascita())
    // .dataCreazione(LocalDateTime.now())
    // .build();

    // utente.setTemaNatale(temaNatale);
    // getRepository().save(utente);
    // return getConverter().fromEToD(utente);
    // }

    public List<UtenteDetail> getAllUsers() {
        return getAll();
    }

    public Optional<UtenteDetail> findById(Long id) {
        return getRepository().findById(id).map(getConverter()::fromEToD);
    }

    @Transactional
    public Utente register(Registrazione registrazione) {

        if (getRepository().findByUsername(registrazione.username()).isPresent()) {
            throw new UsernameAlreadyExistsException("L username esiste già");
        }

        if (getRepository().findByEmail(registrazione.email()).isPresent()) {
            throw new EmailAlreadyExistsException("L'email esiste già");
        }

        Utente utente = Utente.builder()
                .username(registrazione.username())
                .email(registrazione.email())
                .passwordHash(passwordEncoder.encode(registrazione.password()))
                .ruolo(Ruolo.BASE)
                .build();

        return getRepository().save(utente);
    }

}
