package com.azoth.somniazodiaca.dtos;

import com.azoth.somniazodiaca.enums.InterpretazioneEnum;

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
public class RichiestaInterpretazioneDto implements GenericDto {

    private Long id;
    private Long utenteId;
    private Long sognoId;

    private String testoSogno;

    @NotNull(message = "La tipologia di interpretazione è obbligatoria")
    private InterpretazioneEnum interpretazioneEnum;

    @NotBlank(message = "Il prompt è obbligatorio")
    private String prompt;

    @NotBlank(message = "Il testo è obbligatorio")
    private String testo;

    private LocalDateTime scadenzaCache;

    private LocalDateTime createdAt;
}
