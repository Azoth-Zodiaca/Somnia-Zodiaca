package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.InterpretazioneDto;
import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.entities.Sogno;

@Service
public class InterpretazioneConverter implements GenericConverter<Interpretazione, InterpretazioneDto> {

    public Interpretazione fromDToE(InterpretazioneDto d) {
        Interpretazione e = Interpretazione.builder()
                .id(d.getId())
                .prompt(d.getPrompt())
                .testo(d.getTesto())
                .build();

        if (d.getSognoId() != null) {
            Sogno sogno = new Sogno();
            sogno.setId(d.getSognoId());
            e.setSogno(sogno);
        }

        return e;
    }

    public InterpretazioneDto fromEToD(Interpretazione e) {
        return InterpretazioneDto.builder()
                .id(e.getId())
                .sognoId(e.getSogno() != null ? e.getSogno().getId() : null)
                .prompt(e.getPrompt())
                .testo(e.getTesto())
                .build();
    }
}
