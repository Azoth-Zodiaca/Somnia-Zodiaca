package com.azoth.somniazodiaca.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
    name = "segni_zodiacali",
    uniqueConstraints = @UniqueConstraint(name = "uk_segni_zodiacali_nome", columnNames = "nome")
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class SegnoZodiacale extends BaseEntity {

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descrizione", columnDefinition = "TEXT")
    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "elemento_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_segni_zodiacali_elementi")
    )
    private Elemento elemento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "pianeta_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_segni_zodiacali_pianeti")
    )
    private Pianeta pianeta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "metallo_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_segni_zodiacali_metalli")
    )
    private Metallo metallo;
}
