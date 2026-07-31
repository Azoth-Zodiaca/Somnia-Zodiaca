package com.azoth.somniazodiaca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Metallo;
import com.azoth.somniazodiaca.enums.MetalloEnum;

public interface MetalloRepository extends JpaRepository<Metallo, Long> {

    Optional<Metallo> findByMetallo(MetalloEnum metallo);
}
