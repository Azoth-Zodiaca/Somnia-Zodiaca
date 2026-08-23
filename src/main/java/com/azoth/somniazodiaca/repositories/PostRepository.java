package com.azoth.somniazodiaca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.azoth.somniazodiaca.entities.Post;
import com.azoth.somniazodiaca.entities.Utente;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUtenteIdOrderByDataPubblicazioneDesc(Long utenteId);

    void deleteByUtente(Utente utente);

    long countByUtente_Id(Long utenteId);

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

    @EntityGraph(attributePaths = {
            "utente",
            "utente.segnoZodiacale",
            "utente.ascendente",
            "interpretazione",
            "interpretazione.sogno",
            "commenti"
    })
    @Query("""
            SELECT p
            FROM Post p
            WHERE p.utente.id IN (
                SELECT f.seguito.id
                FROM UtenteFollow f
                WHERE f.follower.id = :followerId
            )
            ORDER BY p.dataPubblicazione DESC
            """)
    List<Post> findFeedSeguiti(@Param("followerId") Long followerId);
}
