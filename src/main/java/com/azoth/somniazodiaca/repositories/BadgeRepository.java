package com.azoth.somniazodiaca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Badge;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Optional<Badge> findByCodice(String codice);
}