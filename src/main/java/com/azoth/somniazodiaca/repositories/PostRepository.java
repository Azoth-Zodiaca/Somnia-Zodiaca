package com.azoth.somniazodiaca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Post;
import com.azoth.somniazodiaca.entities.Utente;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUtenteIdOrderByDataPubblicazioneDesc(Long utenteId);

    void deleteByUtente(Utente utente);

    @EntityGraph(attributePaths = {
            "utente",
            "utente.segnoZodiacale",
            "utente.ascendente",
            "interpretazione",
            "interpretazione.sogno",
            "commenti"
    })
    List<Post> findAllByOrderByDataPubblicazioneDesc();

    @EntityGraph(attributePaths = {
            "utente",
            "utente.segnoZodiacale",
            "utente.ascendente",
            "interpretazione",
            "interpretazione.sogno",
            "commenti"
    })
    List<Post> findByUtenteSegnoZodiacaleIdOrderByDataPubblicazioneDesc(
            Long segnoZodiacaleId);
}
