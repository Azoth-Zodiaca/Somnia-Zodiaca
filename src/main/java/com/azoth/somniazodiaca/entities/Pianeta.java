package com.azoth.somniazodiaca.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "pianeti", uniqueConstraints = @UniqueConstraint(name = "uk_pianeti_nome", columnNames = "nome"))
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Pianeta extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "nome", nullable = false, unique = true)
    private Pianeta pianeta;

    @Column(name = "descrizione", columnDefinition = "TEXT")
    private String descrizione;

    @OneToMany(mappedBy = "pianeta", fetch = FetchType.LAZY)
    private Set<SegnoZodiacale> segni;
}
