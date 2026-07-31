package com.azoth.somniazodiaca.dtos;

import com.azoth.somniazodiaca.enums.Modalita;
import com.azoth.somniazodiaca.enums.SegnoZodiacaleEnum;

import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Il segno zodiacale è obbligatorio")
    private SegnoZodiacaleEnum segnoZodiacale;

    @NotNull(message = "La modalità è obbligatoria")
    private Modalita modalita;

    private String descrizione;

    private Long elementoId;
    private Long pianetaId;
    private Long metalloId;
}
