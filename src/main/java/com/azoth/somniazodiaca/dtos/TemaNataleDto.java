package com.azoth.somniazodiaca.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
public class TemaNataleDto {

    private Long id;

    @NotNull(message = "L'id dell'utente è obbligatorio")
    private Long utenteId;

    @NotNull(message = "La data di nascita è obbligatoria")
    private LocalDate dataNascita;

    @NotNull(message = "L'ora di nascita è obbligatoria")
    private LocalTime oraNascita;

    @NotBlank(message = "Il luogo di nascita è obbligatorio")
    @Size(max = 255, message = "Il luogo di nascita può contenere al massimo 255 caratteri")
    private String luogoNascita;

    @NotNull(message = "La latitudine è obbligatoria")
    @DecimalMin(value = "-90.0", message = "La latitudine deve essere compresa tra -90 e 90")
    @DecimalMax(value = "90.0", message = "La latitudine deve essere compresa tra -90 e 90")
    private BigDecimal latitudine;

    @NotNull(message = "La longitudine è obbligatoria")
    @DecimalMin(value = "-180.0", message = "La longitudine deve essere compresa tra -180 e 180")
    @DecimalMax(value = "180.0", message = "La longitudine deve essere compresa tra -180 e 180")
    private BigDecimal longitudine;

    @NotBlank(message = "Il timezone è obbligatorio")
    @Size(max = 100, message = "Il timezone può contenere al massimo 100 caratteri")
    private String timezone;

    private LocalDateTime dataCreazione;
}