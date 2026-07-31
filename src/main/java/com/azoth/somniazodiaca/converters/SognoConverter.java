package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.AggiuntaSognoDto;
import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.entities.Utente;

@Service
public class SognoConverter implements GenericConverter<Sogno, AggiuntaSognoDto> {

    public Sogno fromDToE(AggiuntaSognoDto d) {
        Sogno e = Sogno.builder()
                .id(d.getId())
                .testo(d.getTesto())
                .build();

        if (d.getUtenteId() != null) {
            Utente utente = Utente.builder().id(d.getUtenteId()).build();
            e.setUtente(utente);
        }

        return e;
    }

    public AggiuntaSognoDto fromEToD(Sogno e) {
        return AggiuntaSognoDto.builder()
                .id(e.getId())
                .utenteId(e.getUtente() != null ? e.getUtente().getId() : null)
                .testo(e.getTesto())
                .dataCreazione(e.getCreatedAt())
                .build();
    }
}
