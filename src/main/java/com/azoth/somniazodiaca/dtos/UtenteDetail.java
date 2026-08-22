package com.azoth.somniazodiaca.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.azoth.somniazodiaca.enums.Ruolo;

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
public class UtenteDetail implements GenericDto {

    private Long id;

    @NotBlank(message = "Lo username è obbligatorio")
    private String username;

    @NotBlank(message = "L'email è obbligatoria")
    private String email;

    @NotNull(message = "Il ruolo è obbligatorio")
    private Ruolo ruolo;

    private SegnoZodiacaleDto segnoZodiacale;
    private SegnoZodiacaleDto ascendente;

    @NotNull(message = "Il QI è obbligatorio")
    private Integer qi;

    private LocalDateTime dataRegistrazione;
    private LocalDateTime ultimoAccesso;
    private Integer giorniConsecutivi;
    private LocalDate ultimaRicompensaGiornaliera;
    private TemaNataleDto temaNatale;
}

