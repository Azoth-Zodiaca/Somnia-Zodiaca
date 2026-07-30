package com.azoth.somniazodiaca.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Entity
@Table(name = "temi_natale")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder

public class Sogno extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", referencedColumnName = "id")
    private Utente utente;

    @Column(name = "testo", nullable = false)
    private String testo;

    @Column(name = "scadenza_cache", nullable = false)
    private LocalDateTime scadenzaCache; //valutare se utilizzare ZonedDateTime

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione;

}
