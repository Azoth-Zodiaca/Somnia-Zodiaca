package com.azoth.somniazodiaca.dtos.records;

public record SalvaInterpretazioneRequest(
        String testoSogno,
        String prompt,
        String interpretazione,
        String umore,
        String stile) {
}