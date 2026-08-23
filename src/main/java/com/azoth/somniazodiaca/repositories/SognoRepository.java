package com.azoth.somniazodiaca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.entities.Utente;

public interface SognoRepository extends JpaRepository<Sogno, Long> {

    List<Sogno> findByUtenteId(Long utenteId);

    void deleteByUtente(Utente utente);

    long countByUtente_Id(Long utenteId);
}
