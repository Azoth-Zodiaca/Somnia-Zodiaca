package com.azoth.somniazodiaca.dtos.records;

import java.math.BigDecimal;

public record LocalitaDto(
        Long geonameId,
        String nome,
        String stato,
        String codicePaese,
        BigDecimal latitudine,
        BigDecimal longitudine,
        String timezoneId) {
}