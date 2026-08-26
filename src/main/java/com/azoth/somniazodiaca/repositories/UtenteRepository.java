package com.azoth.somniazodiaca.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.Ruolo;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByEmail(String email);

    List<Utente> findByRuolo(Ruolo ruolo);

    @Query("select u from Utente u where u.username = :usernameOrEmail or u.email = :usernameOrEmail")
    Optional<Utente> findByUsernameOrEmail(String usernameOrEmail);
}
