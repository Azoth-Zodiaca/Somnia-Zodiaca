package com.azoth.somniazodiaca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.entities.Utente;

public interface InterpretazioneRepository extends JpaRepository<Interpretazione, Long> {

    List<Interpretazione> findBySognoId(Long sognoId);

    List<Interpretazione> findBySogno_Utente_IdOrderByCreatedAtDesc(Long utenteId);

    void deleteBySogno_Utente(Utente utente);
}
