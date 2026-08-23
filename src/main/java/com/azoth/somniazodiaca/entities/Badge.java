package com.azoth.somniazodiaca.entities;

import com.azoth.somniazodiaca.enums.TipoCondizione;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "badge")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Badge extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String codice;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 255)
    private String descrizione;

    @Column(nullable = false, length = 100)
    private String icona;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_condizione", nullable = false, length = 50)
    private TipoCondizione tipoCondizione;

    @Column
    private Integer soglia;

    @Column(name = "ricompensa_qi", nullable = false)
    @Builder.Default
    private Integer ricompensaQi = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean attivo = true;
}