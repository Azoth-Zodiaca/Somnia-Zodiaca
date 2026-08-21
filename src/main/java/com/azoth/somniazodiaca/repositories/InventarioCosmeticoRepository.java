package com.azoth.somniazodiaca.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.InventarioCosmetico;
import com.azoth.somniazodiaca.entities.Utente;

public interface InventarioCosmeticoRepository extends JpaRepository<InventarioCosmetico, Long> {

    List<InventarioCosmetico> findByUtenteId(Long utenteId);

    Optional<InventarioCosmetico> findByUtenteIdAndCosmeticoId(Long utenteId, Long cosmeticoId);

    List<InventarioCosmetico> findByUtenteIdAndEquipaggiatoTrue(Long utenteId);

    void deleteByUtente(Utente utente);
}
