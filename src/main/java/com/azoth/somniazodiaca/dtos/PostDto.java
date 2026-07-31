package com.azoth.somniazodiaca.dtos;


import java.time.LocalDateTime;

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
public class PostDto implements GenericDto {

    private Long id;

    @NotNull(message = "L'id dell'utente è obbligatorio")
    private Long utenteId;

    private Long interpretazioneId;

    @NotBlank(message = "Il testo visibile è obbligatorio")
    private String testoVisibile;

    private LocalDateTime dataPubblicazione;

    @NotNull(message = "Il numero di like è obbligatorio")
    private Integer numeroLike;
}
