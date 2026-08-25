package com.azoth.somniazodiaca.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


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
public class TemaNataleDto implements GenericDto {

    private Long id;
    private String rispostaAstroWay;

    @NotNull(message = "L'id dell'utente è obbligatorio")
    private Long utenteId;

    @NotNull(message = "La data di nascita è obbligatoria")
    private LocalDate dataNascita;

    @NotNull(message = "L'ora di nascita è obbligatoria")
    private LocalTime oraNascita;

    @NotBlank(message = "Il luogo di nascita è obbligatorio")
    private String luogoNascita;

    private BigDecimal latitudine;
    private BigDecimal longitudine;
    private String timezone;
    private LocalDateTime dataCreazione;

    private String interpretazioneAstroWay;

    private String analisiGemini;
}
