package com.azoth.somniazodiaca.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class InventarioCosmeticoDto implements GenericDto {

    private Long id;

    @NotNull(message = "L'id dell'utente è obbligatorio")
    private Long utenteId;

    @NotNull(message = "L'id del cosmetico è obbligatorio")
    private Long cosmeticoId;

    @NotNull(message = "Il campo equipaggiato è obbligatorio")
    private Boolean equipaggiato;

    private LocalDateTime dataAcquisto;
}
