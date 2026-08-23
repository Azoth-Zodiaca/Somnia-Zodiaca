package com.azoth.somniazodiaca.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azoth.somniazodiaca.converters.UtenteConverter;
import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.dtos.records.Registrazione;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.exceptions.EmailAlreadyExistsException;
import com.azoth.somniazodiaca.exceptions.UsernameAlreadyExistsException;
import com.azoth.somniazodiaca.repositories.InterpretazioneRepository;
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
    private final InterpretazioneRepository interpretazioneRepository;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    private static final Pattern PASSWORD_FORTE = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$");

    private static final long DIMENSIONE_MASSIMA_IMMAGINE = 5 * 1024 * 1024;

    private static final Set<String> TIPI_IMMAGINE_CONSENTITI = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp");

    @Value("${app.upload-dir}")
    private String uploadDir;

    public UtenteService(
            UtenteRepository repository,
            UtenteConverter converter,
            PasswordEncoder passwordEncoder,
            PostRepository postRepository,
            SognoRepository sognoRepository,
            TemaNataleRepository temaNataleRepository,
            InventarioCosmeticoRepository inventarioCosmeticoRepository,
            InterpretazioneRepository interpretazioneRepository) {

        super(repository, converter);
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.sognoRepository = sognoRepository;
        this.temaNataleRepository = temaNataleRepository;
        this.inventarioCosmeticoRepository = inventarioCosmeticoRepository;
        this.interpretazioneRepository = interpretazioneRepository;
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
    public void registraAccesso(String username) {
        Utente utente = getRepository()
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        LocalDate oggi = LocalDate.now();
        LocalDate ultimoGiorno = utente.getUltimoAccesso() == null
                ? null
                : utente.getUltimoAccesso().toLocalDate();

        if (oggi.equals(ultimoGiorno)) {
            return;
        }

        if (ultimoGiorno != null && ultimoGiorno.plusDays(1).equals(oggi)) {
            utente.setGiorniConsecutivi(utente.getGiorniConsecutivi() + 1);
        } else {
            utente.setGiorniConsecutivi(1);
        }

        utente.setUltimoAccesso(java.time.LocalDateTime.now());

        getRepository().save(utente);
    }

    @Transactional
    public int riscuotiRicompensaGiornaliera(String username) {
        Utente utente = getRepository()
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        LocalDate oggi = LocalDate.now();

        if (oggi.equals(utente.getUltimaRicompensaGiornaliera())) {
            throw new IllegalStateException(
                    "La ricompensa è già stata riscossa oggi");
        }

        int[] ricompense = { 10, 15, 20, 25, 30, 40, 50 };

        int giorno = Math.max(1, utente.getGiorniConsecutivi());
        int indice = Math.min(giorno, ricompense.length) - 1;
        int quantitaQi = ricompense[indice];

        utente.setQi(utente.getQi() + quantitaQi);
        utente.setUltimaRicompensaGiornaliera(oggi);

        getRepository().save(utente);

        return quantitaQi;
    }

    @Transactional
    public UtenteDetail updateAccount(
            String currentUsername,
            String username,
            String email) {

        Utente utente = getRepository()
                .findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        getRepository().findByUsername(username)
                .filter(altroUtente -> !altroUtente.getId().equals(utente.getId()))
                .ifPresent(altroUtente -> {
                    throw new UsernameAlreadyExistsException(
                            "Lo username esiste già");
                });

        getRepository().findByEmail(email)
                .filter(altroUtente -> !altroUtente.getId().equals(utente.getId()))
                .ifPresent(altroUtente -> {
                    throw new EmailAlreadyExistsException(
                            "L'email esiste già");
                });

        utente.setUsername(username);
        utente.setEmail(email);

        return getConverter().fromEToD(
                getRepository().save(utente));
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
        Utente utente = getRepository()
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        temaNataleRepository.deleteByUtente(utente);
        inventarioCosmeticoRepository.deleteByUtente(utente);
        postRepository.deleteByUtente(utente);

        // Prima eliminiamo le interpretazioni collegate ai sogni.
        interpretazioneRepository.deleteBySogno_Utente(utente);

        // Solo dopo possiamo eliminare i sogni.
        sognoRepository.deleteByUtente(utente);

        getRepository().delete(utente);
    }

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

        if (!PASSWORD_FORTE.matcher(registrazione.password()).matches()) {
            throw new IllegalArgumentException(
                    "La password deve essere forte.");
        }

        Utente utente = Utente.builder()
                .username(registrazione.username())
                .email(registrazione.email())
                .passwordHash(passwordEncoder.encode(registrazione.password()))
                .ruolo(Ruolo.BASE)
                .profiloColore(generaColoreProfilo())
                .build();

        return getRepository().save(utente);
    }

    private String generaColoreProfilo() {
        byte[] colore = new byte[3];
        RANDOM.nextBytes(colore);

        return "#" + HEX_FORMAT.formatHex(colore).toUpperCase();
    }

    @Transactional
    public void aggiornaImmagini(
            String username,
            MultipartFile avatar,
            MultipartFile banner) {

        Utente utente = getRepository()
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Utente non trovato"));

        if (avatar != null && !avatar.isEmpty()) {
            validaImmagine(avatar);
            utente.setAvatarPath(salvaImmagine(avatar, "avatar"));
        }

        if (banner != null && !banner.isEmpty()) {
            validaImmagine(banner);
            utente.setBannerPath(salvaImmagine(banner, "banner"));
        }

        getRepository().save(utente);
    }

    private void validaImmagine(MultipartFile file) {
        if (file.getSize() > DIMENSIONE_MASSIMA_IMMAGINE) {
            throw new IllegalArgumentException(
                    "L'immagine non può superare i 5 MB");
        }

        if (!TIPI_IMMAGINE_CONSENTITI.contains(file.getContentType())) {
            throw new IllegalArgumentException(
                    "Formato immagine non supportato");
        }
    }

    private String salvaImmagine(
            MultipartFile file,
            String prefisso) {

        try {
            Path directory = Paths.get(uploadDir);
            Files.createDirectories(directory);

            String estensione = switch (file.getContentType()) {
                case "image/jpeg" -> ".jpg";
                case "image/png" -> ".png";
                case "image/webp" -> ".webp";
                default -> throw new IllegalArgumentException(
                        "Formato immagine non supportato");
            };

            String nomeFile = prefisso + "-"
                    + UUID.randomUUID()
                    + estensione;

            Path destinazione = directory.resolve(nomeFile);
            file.transferTo(destinazione);

            return "/uploads/profiles/" + nomeFile;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Impossibile salvare l'immagine", e);
        }
    }

}
