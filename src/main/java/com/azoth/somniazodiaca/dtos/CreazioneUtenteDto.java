package com.azoth.somniazodiaca.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import com.azoth.somniazodiaca.enums.Ruolo;

import jakarta.validation.constraints.Email;
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
public class CreazioneUtenteDto implements GenericDto {

    private Long id;

    @NotBlank(message = "Lo username è obbligatorio")
    @Size(min = 3, max = 50, message = "Lo username deve essere compreso tra 3 e 50 caratteri")
    private String username;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email non è valida")
    @Size(max = 255, message = "L'email può contenere al massimo 255 caratteri")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 8, message = "La password deve contenere almeno 8 caratteri")
    private String password;
    
    @NotNull(message = "Il ruolo è obbligatorio")
    private Ruolo ruolo;

    @NotNull(message = "La data di nascita è obbligatoria")
    private LocalDate dataNascita;

    @NotNull(message = "L'ora di nascita è obbligatoria")
    private LocalTime oraNascita;

    @NotBlank(message = "Il luogo di nascita è obbligatorio")
    @Size(max = 255, message = "Il luogo di nascita può contenere al massimo 255 caratteri")
    private String luogoNascita;
}