package com.azoth.somniazodiaca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Pianeta;
import com.azoth.somniazodiaca.enums.PianetaEnum;

public interface PianetaRepository extends JpaRepository<Pianeta, Long> {

    Optional<Pianeta> findByPianeta(PianetaEnum pianeta);
}
