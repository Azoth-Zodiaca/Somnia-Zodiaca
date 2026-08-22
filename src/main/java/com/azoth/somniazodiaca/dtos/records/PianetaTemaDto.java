package com.azoth.somniazodiaca.dtos.records;

public record PianetaTemaDto(
        String nome,
        double longitudine,
        String segno,
        Integer casa,
        boolean retrogrado) {
}
