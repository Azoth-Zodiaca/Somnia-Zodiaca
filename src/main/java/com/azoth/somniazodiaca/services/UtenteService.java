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
import com.azoth.somniazodiaca.repositories.InventarioCosmeticoRepository;
import com.azoth.somniazodiaca.repositories.PostRepository;
import com.azoth.somniazodiaca.repositories.SognoRepository;
import com.azoth.somniazodiaca.repositories.TemaNataleRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

import jakarta.transaction.Transactional;

@Service
public class UtenteService extends GenericService<Long, Utente, UtenteDetail, UtenteConverter, UtenteRepository> {

    private final PasswordEncoder passwordEncoder;

    private final PostRepository postRepository;
    private final SognoRepository sognoRepository;
    private final TemaNataleRepository temaNataleRepository;
    private final InventarioCosmeticoRepository inventarioCosmeticoRepository;

    public UtenteService(
            UtenteRepository repository,
            UtenteConverter converter,
            PasswordEncoder passwordEncoder,
            PostRepository postRepository,
            SognoRepository sognoRepository,
            TemaNataleRepository temaNataleRepository,
            InventarioCosmeticoRepository inventarioCosmeticoRepository) {

        super(repository, converter);
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.sognoRepository = sognoRepository;
        this.temaNataleRepository = temaNataleRepository;
        this.inventarioCosmeticoRepository = inventarioCosmeticoRepository;
    }

    public Optional<UtenteDetail> findByUsername(String username) {
        return getRepository().findByUsername(username).map(getConverter()::fromEToD);
    }

    public Optional<UtenteDetail> findByEmail(String email) {
        return getRepository().findByEmail(email).map(getConverter()::fromEToD);
    }

    @Transactional
    public void addQi(String username, int quantitaQi) {
        if (quantitaQi <= 0) {
            throw new IllegalArgumentException("La quantità QI deve essere positiva");
        }

        Utente utente = getRepository().findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        utente.setQi(utente.getQi() + quantitaQi);
        getRepository().save(utente);
    }

    @Transactional
    public UtenteDetail updateAccount(String currentUsername, String username, String email, String nomeVisibile) {
        Utente utente = getRepository().findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        getRepository().findByUsername(username)
                .filter(altroUtente -> !altroUtente.getId().equals(utente.getId()))
                .ifPresent(altroUtente -> {
                    throw new UsernameAlreadyExistsException("L username esiste già");
                });

        getRepository().findByEmail(email)
                .filter(altroUtente -> !altroUtente.getId().equals(utente.getId()))
                .ifPresent(altroUtente -> {
                    throw new EmailAlreadyExistsException("L'email esiste già");
                });

        utente.setUsername(username);
        utente.setEmail(email);
        return getConverter().fromEToD(getRepository().save(utente));
    }

    @Transactional
    public void changePassword(
            String username,
            String passwordAttuale,
            String nuovaPassword,
            String confermaPassword) {

        if (!nuovaPassword.equals(confermaPassword)) {
            throw new IllegalArgumentException(
                    "La conferma della nuova password non coincide");
        }

        if (nuovaPassword.length() < 8) {
            throw new IllegalArgumentException(
                    "La nuova password deve contenere almeno 8 caratteri");
        }

        Utente utente = getRepository().findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (!passwordEncoder.matches(passwordAttuale, utente.getPasswordHash())) {
            throw new IllegalArgumentException("La password attuale non è corretta");
        }

        utente.setPasswordHash(passwordEncoder.encode(nuovaPassword));
        getRepository().save(utente);
    }

    @Transactional
    public void deleteAccount(String username) {
        Utente utente = getRepository().findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        temaNataleRepository.deleteByUtente(utente);
        inventarioCosmeticoRepository.deleteByUtente(utente);
        postRepository.deleteByUtente(utente);
        sognoRepository.deleteByUtente(utente);

        getRepository().delete(utente);
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
