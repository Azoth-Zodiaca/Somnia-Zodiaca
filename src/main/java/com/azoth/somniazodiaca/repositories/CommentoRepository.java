package com.azoth.somniazodiaca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Commento;
import com.azoth.somniazodiaca.entities.Utente;

public interface CommentoRepository extends JpaRepository<Commento, Long> {

    @EntityGraph(attributePaths = "utente")
    List<Commento> findByPostIdOrderByCreatedAtAsc(Long postId);

    void deleteByUtente(Utente utente);

    void deleteByPost_Utente(Utente utente);
}