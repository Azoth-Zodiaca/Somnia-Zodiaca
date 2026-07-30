package com.azoth.somniazodiaca.dtos;

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
public class SegnoZodiacaleDto implements GenericDto {

    private Long id;

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 10, message = "Il nome può contenere al massimo 10 caratteri")
    private String nome;

    private String descrizione;

    @NotNull(message = "L'id dell'elemento è obbligatorio")
    private Long elementoId;

    @NotNull(message = "L'id del pianeta è obbligatorio")
    private Long pianetaId;

    @NotNull(message = "L'id del metallo è obbligatorio")
    private Long metalloId;
}
