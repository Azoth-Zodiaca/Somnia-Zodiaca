package com.azoth.somniazodiaca.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.config.AppDomainProperties;
import com.azoth.somniazodiaca.converters.PostConverter;
import com.azoth.somniazodiaca.dtos.PostDto;
import com.azoth.somniazodiaca.entities.Commento;
import com.azoth.somniazodiaca.entities.LikePost;
import com.azoth.somniazodiaca.entities.Post;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.entities.UtenteFollow;
import com.azoth.somniazodiaca.repositories.InterpretazioneRepository;
import com.azoth.somniazodiaca.repositories.LikePostRepository;
import com.azoth.somniazodiaca.repositories.PostRepository;
import com.azoth.somniazodiaca.repositories.UtenteFollowRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;
import com.azoth.somniazodiaca.repositories.CommentoRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService extends GenericService<Long, Post, PostDto, PostConverter, PostRepository> {

        private final UtenteRepository utenteRepository;
        private final InterpretazioneRepository interpretazioneRepository;
        private final LikePostRepository likePostRepository;
        private final UtenteFollowRepository utenteFollowRepository;
        private final CommentoRepository commentoRepository;
        private final BadgeService badgeService;
        private final AppDomainProperties appDomainProperties;

        public PostService(
                        PostRepository repository,
                        PostConverter converter,
                        UtenteRepository utenteRepository,
                        InterpretazioneRepository interpretazioneRepository,
                        LikePostRepository likePostRepository,
                        UtenteFollowRepository utenteFollowRepository,
                        CommentoRepository commentoRepository,
                        BadgeService badgeService,
                        AppDomainProperties appDomainProperties) {

                super(repository, converter);
                this.utenteRepository = utenteRepository;
                this.interpretazioneRepository = interpretazioneRepository;
                this.likePostRepository = likePostRepository;
                this.utenteFollowRepository = utenteFollowRepository;
                this.commentoRepository = commentoRepository;
                this.badgeService = badgeService;
                this.appDomainProperties = appDomainProperties;
        }

        public List<PostDto> findByUtenteId(Long utenteId) {
                return getRepository().findByUtenteIdOrderByDataPubblicazioneDesc(utenteId).stream()
                                .map(getConverter()::fromEToD).toList();
        }

        @Transactional
        public void creaPost(
                        String username,
                        Long interpretazioneId,
                        String testoVisibile) {
                if (testoVisibile == null || testoVisibile.isBlank()) {
                        throw new IllegalArgumentException("Il testo del post è obbligatorio");
                }

                String testo = testoVisibile.trim();
                if (testo.length() > appDomainProperties.getContenuti().getLimitePostCaratteri()) {
                        throw new IllegalArgumentException("Il testo del post supera il limite configurato");
                }

                var utente = utenteRepository.findByUsername(username)
                                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
                var interpretazione = interpretazioneRepository.findById(interpretazioneId)
                                .orElseThrow(() -> new IllegalArgumentException("Interpretazione non trovata"));

                if (interpretazione.getSogno() == null
                                || interpretazione.getSogno().getUtente() == null
                                || !utente.getId().equals(interpretazione.getSogno().getUtente().getId())) {
                        throw new IllegalArgumentException("L'interpretazione non appartiene all'utente autenticato");
                }

                if (interpretazione.getScadenzaCache() != null) {
                        throw new IllegalStateException(
                                        "Rendi permanente l'interpretazione prima di pubblicarla");
                }

                Post post = Post.builder()
                                .utente(utente)
                                .interpretazione(interpretazione)
                                .testoVisibile(testo)
                                .dataPubblicazione(java.time.LocalDateTime.now())
                                .numeroLike(0)
                                .build();

                getRepository().save(post);
                badgeService.verificaBadge(username);
        }

        @Transactional
        public LikeResult toggleLike(String username, Long postId) {
                var utente = utenteRepository.findByUsername(username)
                                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

                var post = getRepository().findById(postId)
                                .orElseThrow(() -> new IllegalArgumentException("Post non trovato"));

                var likeEsistente = likePostRepository
                                .findByPostIdAndUtenteId(postId, utente.getId());

                boolean likeAggiunto = false;

                if (likeEsistente.isPresent()) {
                        likePostRepository.delete(likeEsistente.get());
                        post.setNumeroLike(Math.max(0, post.getNumeroLike() - 1));
                } else {
                        likePostRepository.save(LikePost.builder()
                                        .post(post)
                                        .utente(utente)
                                        .build());

                        post.setNumeroLike(post.getNumeroLike() + 1);
                        likeAggiunto = true;
                }

                getRepository().save(post);
                if (likeAggiunto) {
                        badgeService.verificaBadge(
                                        post.getUtente().getUsername());
                }

                return new LikeResult(
                                likeAggiunto,
                                post.getNumeroLike());
        }

        @Transactional
        public CommentResult aggiungiCommento(String username, Long postId, String testo) {
                if (testo == null || testo.isBlank()) {
                        throw new IllegalArgumentException("Il commento non può essere vuoto");
                }

                String testoPulito = testo.trim();
                if (testoPulito.length() > appDomainProperties.getContenuti().getLimiteCommentoCaratteri()) {
                        throw new IllegalArgumentException("Il commento supera il limite configurato");
                }

                Post post = getRepository().findById(postId)
                                .orElseThrow(() -> new IllegalArgumentException("Post non trovato"));
                Utente utente = trovaUtente(username);
                Commento commento = Commento.builder()
                                .post(post)
                                .utente(utente)
                                .testo(testoPulito)
                                .build();

                commentoRepository.save(commento);
                badgeService.verificaBadge(username);
                return new CommentResult(utente.getUsername(), testoPulito, post.getCommenti().size());
        }

        public record CommentResult(String username, String testo, int count) {
        }

        public record LikeResult(boolean liked, int count) {
        }

        @Transactional
        public List<PostDto> findFeed(String username) {
                var utente = trovaUtente(username);

                return convertiFeed(
                                getRepository().findAllByOrderByDataPubblicazioneDesc(),
                                utente.getId());
        }

        @Transactional
        public List<PostDto> findFeedMioSegno(String username) {
                var utente = trovaUtente(username);

                if (utente.getSegnoZodiacale() == null) {
                        return List.of();
                }

                return convertiFeed(
                                getRepository()
                                                .findByUtenteSegnoZodiacaleIdOrderByDataPubblicazioneDesc(
                                                                utente.getSegnoZodiacale().getId()),
                                utente.getId());
        }

        private Utente trovaUtente(String username) {
                return utenteRepository.findByUsername(username)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Utente non trovato"));
        }

        private List<PostDto> convertiFeed(
                        List<Post> posts,
                        Long utenteId) {

                return posts.stream()
                                .map(post -> {
                                        PostDto dto = getConverter().fromEToD(post);

                                        dto.setLikedByCurrentUser(
                                                        likePostRepository.existsByPostIdAndUtenteId(
                                                                        post.getId(),
                                                                        utenteId));

                                        dto.setSeguitoDaCurrentUser(
                                                        post.getUtente() != null
                                                                        && !post.getUtente().getId().equals(utenteId)
                                                                        && utenteFollowRepository
                                                                                        .existsByFollowerIdAndSeguitoId(
                                                                                                        utenteId,
                                                                                                        post.getUtente().getId()));

                                        return dto;
                                })
                                .toList();
        }

        @Transactional
        public List<PostDto> findFeedSeguiti(String username) {
                Utente follower = trovaUtente(username);

                return convertiFeed(
                                getRepository().findFeedSeguiti(follower.getId()),
                                follower.getId());
        }

        @Transactional
        public List<Utente> findSeguiti(String username) {
                Utente follower = trovaUtente(username);

                return utenteFollowRepository.findSeguitiByFollowerId(follower.getId());
        }

        @Transactional
        public void segui(String followerUsername, String seguitoUsername) {
                Utente follower = trovaUtente(followerUsername);
                Utente seguito = trovaUtente(seguitoUsername);

                if (follower.getId().equals(seguito.getId())) {
                        throw new IllegalArgumentException(
                                        "Non puoi seguire te stesso");
                }

                boolean giaSeguito = utenteFollowRepository.existsByFollowerIdAndSeguitoId(
                                follower.getId(),
                                seguito.getId());

                if (!giaSeguito) {
                        utenteFollowRepository.save(
                                        UtenteFollow.builder()
                                                        .follower(follower)
                                                        .seguito(seguito)
                                                        .build());
                }
        }

        @Transactional
        public void smettiDiSeguire(
                        String followerUsername,
                        String seguitoUsername) {

                Utente follower = trovaUtente(followerUsername);
                Utente seguito = trovaUtente(seguitoUsername);

                utenteFollowRepository
                                .findByFollowerIdAndSeguitoId(
                                                follower.getId(),
                                                seguito.getId())
                                .ifPresent(utenteFollowRepository::delete);
        }

        public long countLikeRicevuti(Long utenteId) {
                return likePostRepository.countLikeRicevuti(utenteId);
        }
}
