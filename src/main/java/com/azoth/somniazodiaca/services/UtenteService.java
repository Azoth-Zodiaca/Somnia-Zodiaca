package com.azoth.somniazodiaca.services;

import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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
import com.azoth.somniazodiaca.repositories.CommentoRepository;
import com.azoth.somniazodiaca.repositories.LikePostRepository;
import com.azoth.somniazodiaca.repositories.PostRepository;
import com.azoth.somniazodiaca.repositories.SognoRepository;
import com.azoth.somniazodiaca.repositories.TemaNataleRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;
import com.azoth.somniazodiaca.repositories.UtenteBadgeRepository;
import com.azoth.somniazodiaca.repositories.UtenteFollowRepository;

import jakarta.transaction.Transactional;

@Service
public class UtenteService extends GenericService<Long, Utente, UtenteDetail, UtenteConverter, UtenteRepository> {

    private final PasswordEncoder passwordEncoder;

    private final PostRepository postRepository;
    private final SognoRepository sognoRepository;
    private final TemaNataleRepository temaNataleRepository;
    private final InventarioCosmeticoRepository inventarioCosmeticoRepository;
    private final InterpretazioneRepository interpretazioneRepository;
    private final BadgeService badgeService;
    private final UtenteBadgeRepository utenteBadgeRepository;
    private final CommentoRepository commentoRepository;
    private final LikePostRepository likePostRepository;
    private final UtenteFollowRepository utenteFollowRepository;
    

    private static final SecureRandom RANDOM = new SecureRandom();
        private static final List<Color> COLORI_AVATAR = List.of(
            Color.decode("#F97316"),
            Color.decode("#E11D48"),
            Color.decode("#16A34A"),
            Color.decode("#2563EB"),
            Color.decode("#9333EA"));

        private static final int DIMENSIONE_AVATAR_GENERATO = 256;
        private static final int LARGHEZZA_BANNER_GENERATO = 1200;
        private static final int ALTEZZA_BANNER_GENERATO = 320;

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
            InterpretazioneRepository interpretazioneRepository,
            BadgeService badgeService,
            UtenteBadgeRepository utenteBadgeRepository,
            CommentoRepository commentoRepository,
            LikePostRepository likePostRepository,
            UtenteFollowRepository utenteFollowRepository) {

        super(repository, converter);
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
        this.sognoRepository = sognoRepository;
        this.temaNataleRepository = temaNataleRepository;
        this.inventarioCosmeticoRepository = inventarioCosmeticoRepository;
        this.interpretazioneRepository = interpretazioneRepository;
        this.badgeService = badgeService;
        this.utenteBadgeRepository = utenteBadgeRepository;
        this.commentoRepository = commentoRepository;
        this.likePostRepository = likePostRepository;
        this.utenteFollowRepository = utenteFollowRepository;
    }

    public Optional<UtenteDetail> findByUsername(String username) {
        return getRepository().findByUsername(username).map(getConverter()::fromEToD);
    }

    public Optional<UtenteDetail> findByEmail(String email) {
        return getRepository().findByEmail(email).map(getConverter()::fromEToD);
    }

    @Transactional
    public UtenteDetail attivaPremium(String username) {
        Utente utente = getRepository().findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (utente.getRuolo() != Ruolo.BASE) {
            throw new IllegalStateException("L'abbonamento Premium non è attivabile per questo account");
        }

        utente.setRuolo(Ruolo.PREMIUM);
        return getConverter().fromEToD(getRepository().save(utente));
    }

    @Transactional
    public UtenteDetail cancellaPremium(String username) {
        Utente utente = getRepository().findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (utente.getRuolo() != Ruolo.PREMIUM) {
            throw new IllegalStateException("L'account non ha un abbonamento Premium attivo");
        }

        utente.setRuolo(Ruolo.BASE);
        return getConverter().fromEToD(getRepository().save(utente));
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
            int giorniConsecutivi = utente.getGiorniConsecutivi() == null
                    ? 0
                    : utente.getGiorniConsecutivi();
            utente.setGiorniConsecutivi(giorniConsecutivi + 1);
        } else {
            utente.setGiorniConsecutivi(1);
        }

        utente.setUltimoAccesso(java.time.LocalDateTime.now());

        getRepository().save(utente);
        badgeService.verificaBadge(username);
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

        if (utente.getUltimaRicompensaGiornaliera() != null
                && !utente.getUltimaRicompensaGiornaliera().plusDays(1).equals(oggi)) {
            utente.setGiorniRicompensaGiornaliera(0);
        }

        int[] ricompense = { 10, 20, 40, 70, 110, 150, 200 };

        int giorno = Math.min(
            Math.max(0, utente.getGiorniRicompensaGiornaliera() == null
                ? 0
                : utente.getGiorniRicompensaGiornaliera()) + 1,
            ricompense.length);
        int indice = Math.min(giorno, ricompense.length) - 1;
        int quantitaQi = ricompense[indice];

        utente.setQi(utente.getQi() + quantitaQi);
        utente.setGiorniRicompensaGiornaliera(giorno == ricompense.length ? 0 : giorno);
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

        likePostRepository.deleteByUtente(utente);
        likePostRepository.deleteByPost_Utente(utente);
        commentoRepository.deleteByUtente(utente);
        commentoRepository.deleteByPost_Utente(utente);
        utenteFollowRepository.deleteByFollower(utente);
        utenteFollowRepository.deleteBySeguito(utente);
        temaNataleRepository.deleteByUtente(utente);
        inventarioCosmeticoRepository.deleteByUtente(utente);
        postRepository.deleteByUtente(utente);

        // Prima eliminiamo le interpretazioni collegate ai sogni.
        interpretazioneRepository.deleteBySogno_Utente(utente);

        // Solo dopo possiamo eliminare i sogni.
        sognoRepository.deleteByUtente(utente);

        utenteBadgeRepository.deleteByUtente(utente);
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

        Color coloreAvatar = scegliColoreAvatar();
        Utente utente = Utente.builder()
                .username(registrazione.username())
                .email(registrazione.email())
                .passwordHash(passwordEncoder.encode(registrazione.password()))
                .ruolo(Ruolo.BASE)
            .avatarPath(generaAvatar(registrazione.username(), coloreAvatar))
            .bannerPath(generaBanner(coloreAvatar))
                .build();

        return getRepository().save(utente);
    }

        private Color scegliColoreAvatar() {
            return COLORI_AVATAR.get(RANDOM.nextInt(COLORI_AVATAR.size()));
        }

        private String generaAvatar(String username, Color colore) {
        BufferedImage immagine = new BufferedImage(
            DIMENSIONE_AVATAR_GENERATO,
            DIMENSIONE_AVATAR_GENERATO,
            BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = immagine.createGraphics();
        try {
            graphics.setColor(colore);
            graphics.fillRect(0, 0, DIMENSIONE_AVATAR_GENERATO, DIMENSIONE_AVATAR_GENERATO);
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 128));
            graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            String iniziale = username == null || username.isBlank()
                ? "?"
                : username.substring(0, 1).toUpperCase();
            FontMetrics metrics = graphics.getFontMetrics();
            int x = (DIMENSIONE_AVATAR_GENERATO - metrics.stringWidth(iniziale)) / 2;
            int y = (DIMENSIONE_AVATAR_GENERATO - metrics.getHeight()) / 2
                + metrics.getAscent();
            graphics.drawString(iniziale, x, y);
        } finally {
            graphics.dispose();
        }

        return salvaImmagine(immagine, "avatar");
    }

    private String generaBanner(Color colore) {
        BufferedImage immagine = new BufferedImage(
                LARGHEZZA_BANNER_GENERATO,
                ALTEZZA_BANNER_GENERATO,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = immagine.createGraphics();
        try {
            graphics.setColor(colore);
            graphics.fillRect(0, 0, LARGHEZZA_BANNER_GENERATO, ALTEZZA_BANNER_GENERATO);
        } finally {
            graphics.dispose();
        }

        return salvaImmagine(immagine, "banner");
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
        public void completaAvatarEsistenti() {
        List<Utente> utentiDaAggiornare = getRepository().findAll().stream()
            .filter(utente -> utente.getAvatarPath() == null
                || utente.getAvatarPath().isBlank())
                .peek(utente -> {
                    Color colore = scegliColoreAvatar();
                    utente.setAvatarPath(generaAvatar(utente.getUsername(), colore));
                    if (utente.getBannerPath() == null || utente.getBannerPath().isBlank()) {
                        utente.setBannerPath(generaBanner(colore));
                    }
                })
                .toList();

        if (!utentiDaAggiornare.isEmpty()) {
            getRepository().saveAll(utentiDaAggiornare);
        }
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

    private String salvaImmagine(BufferedImage immagine, String prefisso) {
        try {
            Path directory = Paths.get(uploadDir);
            Files.createDirectories(directory);

            String nomeFile = prefisso + "-"
                    + UUID.randomUUID()
                    + ".png";
            Path destinazione = directory.resolve(nomeFile);
            ImageIO.write(immagine, "png", destinazione.toFile());

            return "/uploads/profiles/" + nomeFile;
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Impossibile salvare l'avatar generato", e);
        }
    }

}
