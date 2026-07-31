package com.azoth.somniazodiaca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Interpretazione;

public interface InterpretazioneRepository extends JpaRepository<Interpretazione, Long> {

    List<Interpretazione> findBySognoId(Long sognoId);
}
