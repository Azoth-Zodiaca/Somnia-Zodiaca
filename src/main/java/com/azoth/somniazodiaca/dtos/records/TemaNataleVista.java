package com.azoth.somniazodiaca.dtos.records;

import java.util.List;

public record TemaNataleVista(
        List<PianetaTemaDto> pianeti,
        CaseTemaDto caseAstrologiche,
        String segnoAscendente,
        List<String> aspetti) {
}
