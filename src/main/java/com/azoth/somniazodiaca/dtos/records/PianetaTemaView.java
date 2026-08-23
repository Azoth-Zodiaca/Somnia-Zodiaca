package com.azoth.somniazodiaca.dtos.records;

public record PianetaTemaView(
        String nome,
        String segno,
        double longitudine,
        boolean retrogrado,
        String elemento,
        String modalita,
        int casa) {
}