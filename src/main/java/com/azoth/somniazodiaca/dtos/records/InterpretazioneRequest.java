package com.azoth.somniazodiaca.dtos.records;

public record InterpretazioneRequest(
        String testoSogno,
        String umore,
        String stile,
        boolean usaTemaNatale) {
}