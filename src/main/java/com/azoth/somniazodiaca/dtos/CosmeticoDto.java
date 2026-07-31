package com.azoth.somniazodiaca.dtos;

import java.util.List;

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
public class CosmeticoDto implements GenericDto {

    private Long id;

    @NotBlank(message = "Il nome del cosmetico è obbligatorio")
    private String nome;

    private String descrizione;

    @NotNull(message = "Il prezzo QI è obbligatorio")
    private Integer prezzoQi;

    private List<Long> inventariIds;
}
