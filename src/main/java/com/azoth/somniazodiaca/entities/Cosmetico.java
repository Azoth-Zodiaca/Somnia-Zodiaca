package com.azoth.somniazodiaca.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cosmetici")
public class Cosmetico extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "prezzo_qi")
    private Integer prezzoQi;

    @Column(length = 255)
    private String asset;

    @Column(nullable = false)
    @Builder.Default
    private Boolean attivo = true;

    // Relazione uno-a-molti verso l'inventario per tracciare chi possiede questo cosmetico
    @OneToMany(mappedBy = "cosmetico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventarioCosmetico> inventari;
}

