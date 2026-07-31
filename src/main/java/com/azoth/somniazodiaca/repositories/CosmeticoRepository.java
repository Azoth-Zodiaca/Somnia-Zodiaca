package com.azoth.somniazodiaca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Cosmetico;

public interface CosmeticoRepository extends JpaRepository<Cosmetico, Long> {

    Optional<Cosmetico> findByNome(String nome);
}
