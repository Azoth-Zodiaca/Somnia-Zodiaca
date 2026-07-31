package com.azoth.somniazodiaca.dtos;

import java.util.List;

import com.azoth.somniazodiaca.enums.PianetaEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class PianetaDto implements GenericDto {

    private Long id;

    @NotNull(message = "Il pianeta è obbligatorio")
    private PianetaEnum pianeta;

    private String descrizione;

    private List<Long> segniIds;
}
