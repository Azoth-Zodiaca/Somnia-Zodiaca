package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.SognoDto;
import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.entities.Utente;

@Service
public class SognoConverter implements GenericConverter<Sogno, SognoDto> {

    public Sogno fromDToE(SognoDto d) {
        Sogno e = Sogno.builder()
                .id(d.getId())
                .testo(d.getTesto())
                .build();

        if (d.getUtenteId() != null) {
            Utente utente = new Utente();
            utente.setId(d.getUtenteId());
            e.setUtente(utente);
        }

        return e;
    }

    public SognoDto fromEToD(Sogno e) {
        return SognoDto.builder()
                .id(e.getId())
                .utenteId(e.getUtente() != null ? e.getUtente().getId() : null)
                .testo(e.getTesto())
                .dataCreazione(e.getDataCreazione())
                .build();
    }
}
