package com.azoth.somniazodiaca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.SegnoZodiacale;
import com.azoth.somniazodiaca.enums.SegnoZodiacaleEnum;

public interface SegnoZodiacaleRepository extends JpaRepository<SegnoZodiacale, Long> {

    Optional<SegnoZodiacale> findBySegnoZodiacale(SegnoZodiacaleEnum segnoZodiacale);
}
