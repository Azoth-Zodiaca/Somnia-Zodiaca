package com.azoth.somniazodiaca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.azoth.somniazodiaca.entities.LikePost;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {

    Optional<LikePost> findByPostIdAndUtenteId(Long postId, Long utenteId);

    boolean existsByPostIdAndUtenteId(Long postId, Long utenteId);

    long countByPostId(Long postId);

    @Query("""
        SELECT COUNT(l)
        FROM LikePost l
        WHERE l.post.utente.id = :utenteId
    """)
    long countLikeRicevuti(@Param("utenteId") Long utenteId);
}