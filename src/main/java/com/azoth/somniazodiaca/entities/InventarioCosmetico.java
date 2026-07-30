package com.azoth.somniazodiaca.entities;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "inventario_cosmetici")
public class InventarioCosmetico extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "cosmetico_id", nullable = false)
    private Cosmetico cosmetico;

    @Column(nullable = false)
    @Builder.Default
    private Boolean equipaggiato = false;

    @Column(name = "data_acquisto", nullable = false)
    @Builder.Default
    private LocalDateTime dataAcquisto = LocalDateTime.now();
}
