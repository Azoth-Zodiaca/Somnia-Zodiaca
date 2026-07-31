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
@Table(name = "metalli", uniqueConstraints = @UniqueConstraint(name = "uk_metalli_nome", columnNames = "nome"))
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Metallo extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "nome", nullable = false, unique = true)
    private Metallo metallo;
    
    @Column(name = "descrizione", columnDefinition = "TEXT")
    private String descrizione;

    @OneToMany(mappedBy = "metallo", fetch = FetchType.LAZY)
    private Set<SegnoZodiacale> segni;
}
