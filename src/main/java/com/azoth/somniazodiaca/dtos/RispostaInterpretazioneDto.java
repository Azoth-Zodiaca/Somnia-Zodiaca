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
public class RispostaInterpretazioneDto implements GenericDto {

    private Long utenteId;    

    @NotBlank
    private InterpretazioneEnum interpretazioneEnum;

    @NotBlank
    private String testo;
}
