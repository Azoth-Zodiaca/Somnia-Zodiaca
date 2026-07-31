package com.azoth.somniazodiaca.entities;

import com.azoth.somniazodiaca.enums.ElementoEnum;
import com.azoth.somniazodiaca.enums.InterpretazioneEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "interpretazioni")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Interpretazione extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sogno_id", referencedColumnName = "id")
    private Sogno sogno;

    @Column(name = "prompt", nullable = false, columnDefinition = "TEXT")
    private String prompt;

    @Column(name = "testo", nullable = false, columnDefinition = "TEXT")
    private String testo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, unique = true)
    private InterpretazioneEnum interpretazioneEnum;

    // @Column(name = "scadenza_cache", nullable = false)
    // @Builder.Default
    // private LocalDateTime scadenzaCache = LocalDateTime.now().plusDays(1);
    // //valutare se utilizzare ZonedDateTime
}
