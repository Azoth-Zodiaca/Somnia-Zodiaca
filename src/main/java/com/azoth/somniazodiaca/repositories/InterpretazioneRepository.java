package com.azoth.somniazodiaca.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.entities.Utente;

public interface InterpretazioneRepository extends JpaRepository<Interpretazione, Long> {

        List<Interpretazione> findBySognoId(Long sognoId);

        List<Interpretazione> findBySogno_Utente_IdAndScadenzaCacheAfterOrderByCreatedAtDesc(
                        Long utenteId,
                        LocalDateTime data);

        void deleteBySogno_Utente(Utente utente);

        long countBySogno_Utente_Id(Long utenteId);

        long countBySogno_Utente_IdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            Long utenteId,
            LocalDateTime inizioSettimana,
            LocalDateTime fineSettimana);

        @Query("""
                        SELECT i
                        FROM Interpretazione i
                        WHERE i.sogno.utente.id = :utenteId
                          AND (
                              i.scadenzaCache IS NULL
                              OR i.scadenzaCache > :data
                          )
                        ORDER BY i.createdAt DESC
                        """)
        List<Interpretazione> findDisponibiliByUtenteId(
                        @Param("utenteId") Long utenteId,
                        @Param("data") LocalDateTime data);

        @Query("""
                        SELECT i
                        FROM Interpretazione i
                        WHERE i.sogno.utente.id = :utenteId
                          AND i.scadenzaCache IS NOT NULL
                          AND i.scadenzaCache > :adesso
                        ORDER BY i.scadenzaCache ASC
                        """)
        List<Interpretazione> findProssimeInScadenza(
                        @Param("utenteId") Long utenteId,
                        @Param("adesso") LocalDateTime adesso,
                        Pageable pageable);
}
