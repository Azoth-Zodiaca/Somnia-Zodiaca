package com.azoth.somniazodiaca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.UtenteFollow;

public interface UtenteFollowRepository
        extends JpaRepository<UtenteFollow, Long> {

    boolean existsByFollowerIdAndSeguitoId(
            Long followerId,
            Long seguitoId);

    Optional<UtenteFollow> findByFollowerIdAndSeguitoId(
            Long followerId,
            Long seguitoId);
}