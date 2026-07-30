package com.azoth.somniazodiaca.entities;

import java.time.LocalDateTime;

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

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Post extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", referencedColumnName = "id")
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interpretazione_id", referencedColumnName = "id")
    private Interpretazione interpretazione;

    @Column(name = "testo_visibile", nullable = false, columnDefinition = "TEXT")
    private String testoVisibile;

    @Column(name = "data_pubblicazione", nullable = false)
    private LocalDateTime dataPubblicazione;

    @Column(name = "numero_like", nullable = false)
    private Integer numeroLike;
}
