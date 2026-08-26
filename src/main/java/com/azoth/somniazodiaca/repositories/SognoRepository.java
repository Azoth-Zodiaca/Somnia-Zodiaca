package com.azoth.somniazodiaca.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.entities.Utente;

public interface SognoRepository extends JpaRepository<Sogno, Long> {

    List<Sogno> findByUtenteId(Long utenteId);

    void deleteByUtente(Utente utente);

    long countByUtente_Id(Long utenteId);

    @Query("""
            SELECT COUNT(DISTINCT i.sogno.id)
            FROM Interpretazione i
            WHERE i.sogno.utente.id = :utenteId
              AND (
                  i.scadenzaCache IS NULL
                  OR i.scadenzaCache > :data
              )
            """)
    long countSogniDisponibiliByUtenteId(
            @Param("utenteId") Long utenteId,
            @Param("data") LocalDateTime data);
}
