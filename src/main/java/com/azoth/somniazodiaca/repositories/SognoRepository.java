package com.azoth.somniazodiaca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Sogno;

public interface SognoRepository extends JpaRepository<Sogno, Long> {
    
}
