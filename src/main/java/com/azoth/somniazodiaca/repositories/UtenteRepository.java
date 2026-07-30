package com.azoth.somniazodiaca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

}
