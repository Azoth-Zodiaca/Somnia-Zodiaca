package com.azoth.somniazodiaca.entities;

import java.time.LocalDateTime;

import com.azoth.somniazodiaca.enums.Ruolo;

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
import lombok.Builder;
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

    /**
     * Contiene soltanto l'hash della password, mai la password in chiaro.
     * Il prefisso {bcrypt} permette al DelegatingPasswordEncoder di riconoscere
     * l'algoritmo usato e rende più semplice una futura migrazione degli hash.
     */
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

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
    @Builder.Default
    private Integer qi = 0;

    @Column(name = "ultimo_accesso")
    private LocalDateTime ultimoAccesso;

    @OneToOne(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TemaNatale temaNatale;
}
