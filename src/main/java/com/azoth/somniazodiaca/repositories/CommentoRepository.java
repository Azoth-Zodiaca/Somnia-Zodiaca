package com.azoth.somniazodiaca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Commento;

public interface CommentoRepository extends JpaRepository<Commento, Long> {

    @EntityGraph(attributePaths = "utente")
    List<Commento> findByPostIdOrderByCreatedAtAsc(Long postId);
}