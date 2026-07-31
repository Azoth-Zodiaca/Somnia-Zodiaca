package com.azoth.somniazodiaca.dtos;

import com.azoth.somniazodiaca.enums.InterpretazioneEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RichiestaInterpretazioneDto implements GenericDto {

    private Long utenteId;    

    @NotBlank(message = "La tipologia di interpretazione è obbligatoria")
    private InterpretazioneEnum interpretazioneEnum;

    @NotBlank(message = "Il testo è obbligatorio")
    private String testo;
}
