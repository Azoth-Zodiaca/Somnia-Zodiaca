package com.azoth.somniazodiaca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Elemento;
import com.azoth.somniazodiaca.enums.ElementoEnum;

public interface ElementoRepository extends JpaRepository<Elemento, Long> {

    Optional<Elemento> findByElemento(ElementoEnum elemento);
}
