package com.azoth.somniazodiaca.converters;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.PianetaDto;
import com.azoth.somniazodiaca.entities.Pianeta;
import com.azoth.somniazodiaca.entities.SegnoZodiacale;

@Service
public class PianetaConverter implements GenericConverter<Pianeta, PianetaDto> {

    public Pianeta fromDToE(PianetaDto d) {
        Pianeta e = Pianeta.builder()
                .id(d.getId())
                .nome(d.getNome())
                .descrizione(d.getDescrizione())
                .build();

        if (d.getSegniIds() != null) {
            Set<SegnoZodiacale> segni = d.getSegniIds().stream()
                    .map(id -> {
                        SegnoZodiacale segno = new SegnoZodiacale();
                        segno.setId(id);
                        return segno;
                    })
                    .collect(Collectors.toSet());
            e.setSegni(segni);
        }

        return e;
    }

    public PianetaDto fromEToD(Pianeta e) {
        PianetaDto d = PianetaDto.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descrizione(e.getDescrizione())
                .build();

        if (e.getSegni() != null) {
            d.setSegniIds(e.getSegni().stream()
                    .map(SegnoZodiacale::getId)
                    .collect(Collectors.toList()));
        }

        return d;
    }
}
