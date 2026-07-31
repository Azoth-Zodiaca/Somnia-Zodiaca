package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.RichiestaInterpretazioneDto;
import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.entities.Sogno;

@Service
public class InterpretazioneConverter implements GenericConverter<Interpretazione, RichiestaInterpretazioneDto> {

    public Interpretazione fromDToE(RichiestaInterpretazioneDto d) {
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

    public RichiestaInterpretazioneDto fromEToD(Interpretazione e) {
        return RichiestaInterpretazioneDto.builder()
                .id(e.getId())
                .sognoId(e.getSogno() != null ? e.getSogno().getId() : null)
                .prompt(e.getPrompt())
                .testo(e.getTesto())
                .build();
    }
}
