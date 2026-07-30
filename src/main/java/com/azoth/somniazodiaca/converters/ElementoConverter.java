package com.azoth.somniazodiaca.converters;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.ElementoDto;
import com.azoth.somniazodiaca.entities.Elemento;
import com.azoth.somniazodiaca.entities.SegnoZodiacale;

@Service
public class ElementoConverter implements GenericConverter<Elemento, ElementoDto> {

    public Elemento fromDToE(ElementoDto d) {
        Elemento e = Elemento.builder()
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

    public ElementoDto fromEToD(Elemento e) {
        ElementoDto d = ElementoDto.builder()
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
