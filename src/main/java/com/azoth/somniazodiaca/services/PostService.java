package com.azoth.somniazodiaca.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.PostConverter;
import com.azoth.somniazodiaca.dtos.PostDto;
import com.azoth.somniazodiaca.entities.LikePost;
import com.azoth.somniazodiaca.entities.Post;
import com.azoth.somniazodiaca.repositories.InterpretazioneRepository;
import com.azoth.somniazodiaca.repositories.LikePostRepository;
import com.azoth.somniazodiaca.repositories.PostRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService extends GenericService<Long, Post, PostDto, PostConverter, PostRepository> {

    private final UtenteRepository utenteRepository;
    private final InterpretazioneRepository interpretazioneRepository;
    private final LikePostRepository likePostRepository;

    public PostService(
            PostRepository repository,
            PostConverter converter,
            UtenteRepository utenteRepository,
            InterpretazioneRepository interpretazioneRepository,
            LikePostRepository likePostRepository) {

        super(repository, converter);
        this.utenteRepository = utenteRepository;
        this.interpretazioneRepository = interpretazioneRepository;
        this.likePostRepository = likePostRepository;
    }

    public List<PostDto> findByUtenteId(Long utenteId) {
        return getRepository().findByUtenteIdOrderByDataPubblicazioneDesc(utenteId).stream()
                .map(getConverter()::fromEToD).toList();
    }

    @Transactional
    public List<PostDto> findFeed(String username) {
        var utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        return getRepository()
                .findAllByOrderByDataPubblicazioneDesc()
                .stream()
                .map(post -> {
                    PostDto dto = getConverter().fromEToD(post);
                    dto.setLikedByCurrentUser(
                            likePostRepository.existsByPostIdAndUtenteId(
                                    post.getId(),
                                    utente.getId()));
                    return dto;
                })
                .toList();
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
        if (testo.length() > 1000) {
            throw new IllegalArgumentException("Il testo del post non può superare 1000 caratteri");
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

        Post post = Post.builder()
                .utente(utente)
                .interpretazione(interpretazione)
                .testoVisibile(testo)
                .dataPubblicazione(java.time.LocalDateTime.now())
                .numeroLike(0)
                .build();

        getRepository().save(post);
    }

    @Transactional
    public void toggleLike(String username, Long postId) {
        var utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        var post = getRepository().findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post non trovato"));

        var likeEsistente = likePostRepository
                .findByPostIdAndUtenteId(postId, utente.getId());

        if (likeEsistente.isPresent()) {
            likePostRepository.delete(likeEsistente.get());
            post.setNumeroLike(Math.max(0, post.getNumeroLike() - 1));
        } else {
            likePostRepository.save(LikePost.builder()
                    .post(post)
                    .utente(utente)
                    .build());

            post.setNumeroLike(post.getNumeroLike() + 1);
        }

        getRepository().save(post);
    }
}
