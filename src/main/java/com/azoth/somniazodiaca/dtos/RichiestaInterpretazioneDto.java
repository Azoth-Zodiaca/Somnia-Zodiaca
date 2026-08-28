package com.azoth.somniazodiaca.dtos;

import java.time.LocalDateTime;

import com.azoth.somniazodiaca.enums.StileEnum;
import com.azoth.somniazodiaca.enums.UmoreEnum;

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

    private Long id;
    private Long utenteId;
    private Long sognoId;

    private String testoSogno;

    private UmoreEnum umore;

    private StileEnum stile;

    @NotBlank(message = "Il prompt è obbligatorio")
    private String prompt;

    @NotBlank(message = "Il testo è obbligatorio")
    private String testo;

    private LocalDateTime scadenzaCache;

    private LocalDateTime createdAt;
}
