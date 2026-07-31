package com.azoth.somniazodiaca.dtos;

import java.util.List;

import com.azoth.somniazodiaca.enums.MetalloEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class MetalloDto implements GenericDto {

    private Long id;

    @NotNull(message = "Il metallo è obbligatorio")
    private MetalloEnum metallo;

    private String descrizione;

    private List<Long> segniIds;
}
