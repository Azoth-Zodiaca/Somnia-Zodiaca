package com.azoth.somniazodiaca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUtenteIdOrderByDataPubblicazioneDesc(Long utenteId);
}
