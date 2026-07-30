package com.azoth.somniazodiaca.entities;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "utenti")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Utente extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo", nullable = false)
    private Ruolo ruolo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segno_zodiacale_id", referencedColumnName = "id")
    private SegnoZodiacale segnoZodiacale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ascendente_id", referencedColumnName = "id")
    private SegnoZodiacale ascendente;

    @Column(name = "qi", nullable = false)
    private Integer qi;

    @Column(name = "data_registrazione", nullable = false)
    private LocalDateTime dataRegistrazione;

    @Column(name = "ultimo_accesso")
    private LocalDateTime ultimoAccesso;

    @OneToOne(mappedBy = "utenti", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TemaNatale temaNatale;
}
