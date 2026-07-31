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
                .elemento(d.getElemento())
                .descrizione(d.getDescrizione())
                .build();

        if (d.getSegniIds() != null) {
            Set<SegnoZodiacale> segni = d.getSegniIds().stream()
                    .map(id -> SegnoZodiacale.builder().id(id).build())
                    .collect(Collectors.toSet());
            e.setSegni(segni);
        }

        return e;
    }

    public ElementoDto fromEToD(Elemento e) {
        ElementoDto d = ElementoDto.builder()
                .id(e.getId())
                .elemento(e.getElemento())
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
