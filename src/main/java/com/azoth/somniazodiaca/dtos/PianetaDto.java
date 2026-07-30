package com.azoth.somniazodiaca.dtos;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 100, message = "Il nome può contenere al massimo 100 caratteri")
    private String nome;

    private String descrizione;

    private List<Long> segniIds;
}
