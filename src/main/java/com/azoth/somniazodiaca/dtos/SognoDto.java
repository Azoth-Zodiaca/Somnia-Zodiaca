package com.azoth.somniazodiaca.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SognoDto implements GenericDto {

    private Long id;

    @NotNull(message = "L'id dell'utente è obbligatorio")
    private Long utenteId;

    @NotBlank(message = "Il testo del sogno è obbligatorio")
    private String testo;

    private LocalDateTime dataCreazione;
}
