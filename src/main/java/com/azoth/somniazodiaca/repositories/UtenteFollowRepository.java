package com.azoth.somniazodiaca.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.entities.UtenteFollow;

public interface UtenteFollowRepository
        extends JpaRepository<UtenteFollow, Long> {

    boolean existsByFollowerIdAndSeguitoId(
            Long followerId,
            Long seguitoId);

    Optional<UtenteFollow> findByFollowerIdAndSeguitoId(
            Long followerId,
            Long seguitoId);

        void deleteByFollower(Utente utente);

        void deleteBySeguito(Utente utente);

    @Query("""
            SELECT f.seguito
            FROM UtenteFollow f
            WHERE f.follower.id = :followerId
            ORDER BY f.seguito.username
            """)
    List<Utente> findSeguitiByFollowerId(@Param("followerId") Long followerId);
}