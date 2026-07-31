package com.azoth.somniazodiaca.dtos;

import java.util.List;

import com.azoth.somniazodiaca.enums.ElementoEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ElementoDto implements GenericDto {

    private Long id;

    @NotNull(message = "L'elemento è obbligatorio")
    private ElementoEnum elemento;

    private String descrizione;

    private List<Long> segniIds;
}
