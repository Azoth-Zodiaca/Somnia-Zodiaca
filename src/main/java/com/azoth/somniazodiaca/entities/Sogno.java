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

@Entity
@Table(name = "sogni")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder

public class Sogno extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", referencedColumnName = "id")
    private Utente utente;

    @Column(name = "testo", nullable = false, columnDefinition = "TEXT")
    private String testo;

}
