package com.azoth.somniazodiaca.dtos;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 100, message = "Il nome può contenere al massimo 100 caratteri")
    private String nome;

    private String descrizione;

    @NotNull(message = "Il prezzo in QI è obbligatorio")
    @Min(value = 0, message = "Il prezzo non può essere negativo")
    private Integer prezzoQi;

    private List<Long> inventariIds;
}
