package com.azoth.somniazodiaca.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "temi_natali")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class TemaNatale extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utente_id", nullable = false, unique = true)
    private Utente utente;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Column(name = "ora_nascita", nullable = false)
    private LocalTime oraNascita;

    @Column(name = "luogo_nascita", nullable = false)
    private String luogoNascita;

    @Column(name = "latitudine", precision = 10, scale = 7)
    private BigDecimal latitudine;

    @Column(name = "longitudine", precision = 10, scale = 7)
    private BigDecimal longitudine;

    @Column(name = "timezone", length = 100)
    private String timezone;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione;

    // AstroWay Lob è Large Object
    @Lob
    @Column(name = "risposta_astroway", columnDefinition = "LONGTEXT")
    private String rispostaAstroWay;

    @Lob
    @Column(name = "interpretazione_astroway", columnDefinition = "LONGTEXT")
    private String interpretazioneAstroWay;

    @Lob
    @Column(name = "analisi_gemini", columnDefinition = "LONGTEXT")
    private String analisiGemini;

    @Column(name = "geoname_id")
    private Long geonameId;
}