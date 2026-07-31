package com.azoth.somniazodiaca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.TemaNatale;

public interface TemaNataleRepository extends JpaRepository<TemaNatale, Long> {

    Optional<TemaNatale> findByUtenteId(Long utenteId);
}
