package com.azoth.somniazodiaca.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RispostaInterpretazioneDto implements GenericDto {

    private Long utenteId;    

    @NotBlank
    private Enum tipologia;

    @NotBlank
    private String testo;
}
