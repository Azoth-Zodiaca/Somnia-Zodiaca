package com.azoth.somniazodiaca.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.azoth.somniazodiaca.entities.Utente;

import com.azoth.somniazodiaca.entities.UtenteBadge;

public interface UtenteBadgeRepository
        extends JpaRepository<UtenteBadge, Long> {

    boolean existsByUtente_IdAndBadge_Id(
            Long utenteId,
            Long badgeId);

    Optional<UtenteBadge> findByUtente_IdAndBadge_Id(
            Long utenteId,
            Long badgeId);

    List<UtenteBadge> findByUtente_IdOrderByCreatedAtDesc(
            Long utenteId);

    long countByUtente_Id(Long utenteId);
    void deleteByUtente(Utente utente);
}