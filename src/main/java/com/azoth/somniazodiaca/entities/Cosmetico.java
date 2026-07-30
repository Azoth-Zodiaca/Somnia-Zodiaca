package com.azoth.somniazodiaca.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "cosmetici")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Cosmetico extends BaseEntity {

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descrizione", columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "prezzo_qi", nullable = false)
    private Integer prezzoQi;

    // Relazione uno-a-molti verso l'inventario per tracciare chi possiede questo cosmetico
    @OneToMany(mappedBy = "cosmetico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventarioCosmetico> inventari;
}

