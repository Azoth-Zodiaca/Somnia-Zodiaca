package com.azoth.somniazodiaca.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class InterpretazioneDto implements GenericDto {

    private Long id;

    @NotNull(message = "L'id del sogno è obbligatorio")
    private Long sognoId;

    @NotBlank(message = "Il prompt è obbligatorio")
    private String prompt;

    @NotBlank(message = "Il testo è obbligatorio")
    private String testo;
}
