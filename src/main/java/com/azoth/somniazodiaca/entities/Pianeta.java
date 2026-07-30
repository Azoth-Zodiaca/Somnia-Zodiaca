package com.azoth.somniazodiaca.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "pianeti",
    uniqueConstraints = @UniqueConstraint(name = "uk_pianeti_nome", columnNames = "nome")
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Pianeta extends BaseEntity {

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descrizione", columnDefinition = "TEXT")
    private String descrizione;
}
