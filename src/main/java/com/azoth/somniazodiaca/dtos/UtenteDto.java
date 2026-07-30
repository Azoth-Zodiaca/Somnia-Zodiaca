package com.azoth.somniazodiaca.dtos;

import java.time.LocalDateTime;

import com.azoth.somniazodiaca.enums.Ruolo;

import jakarta.validation.constraints.Email;
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
public class UtenteDto {

    private Long id;

    @NotBlank(message = "Lo username è obbligatorio")
    @Size(min = 3, max = 50, message = "Lo username deve essere compreso tra 3 e 50 caratteri")
    private String username;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email non è valida")
    @Size(max = 255, message = "L'email può contenere al massimo 255 caratteri")
    private String email;

    @NotNull(message = "Il ruolo è obbligatorio")
    private Ruolo ruolo;

    @NotNull(message = "Il QI è obbligatorio")
    @Min(value = 0, message = "Il QI non può essere negativo")
    private Integer qi;

    private LocalDateTime dataRegistrazione;

    private LocalDateTime ultimoAccesso;

    private TemaNataleDto temaNatale;
}