package com.azoth.somniazodiaca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.azoth.somniazodiaca.entities.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByEmail(String email);

    @Query("select u from Utente u where u.username = :usernameOrEmail or u.email = :usernameOrEmail")
    Optional<Utente> findByUsernameOrEmail(String usernameOrEmail);
}
