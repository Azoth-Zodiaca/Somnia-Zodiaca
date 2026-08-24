package com.azoth.somniazodiaca.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CommentoDto implements GenericDto {

    private Long id;
    private String username;
    private String testo;
    private LocalDateTime dataCreazione;
}