package com.azoth.somniazodiaca.dtos;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record BadgeViewDto(
        String codice,
        String nome,
        String descrizione,
        String icona,
        boolean sbloccato,
        long progresso,
        Integer soglia,
        Integer ricompensaQi,
        LocalDateTime ottenutoIl) {
}