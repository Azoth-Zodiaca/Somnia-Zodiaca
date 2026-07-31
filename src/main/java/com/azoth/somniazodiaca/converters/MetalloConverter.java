package com.azoth.somniazodiaca.converters;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.MetalloDto;
import com.azoth.somniazodiaca.entities.Metallo;
import com.azoth.somniazodiaca.entities.SegnoZodiacale;

@Service
public class MetalloConverter implements GenericConverter<Metallo, MetalloDto> {

    public Metallo fromDToE(MetalloDto d) {
        Metallo e = Metallo.builder()
                .id(d.getId())
                .metallo(d.getMetallo())
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

    public MetalloDto fromEToD(Metallo e) {
        MetalloDto d = MetalloDto.builder()
                .id(e.getId())
                .metallo(e.getMetallo())
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
