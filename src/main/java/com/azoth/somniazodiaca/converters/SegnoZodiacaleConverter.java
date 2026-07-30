package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.SegnoZodiacaleDto;
import com.azoth.somniazodiaca.entities.Elemento;
import com.azoth.somniazodiaca.entities.Metallo;
import com.azoth.somniazodiaca.entities.Pianeta;
import com.azoth.somniazodiaca.entities.SegnoZodiacale;

@Service
public class SegnoZodiacaleConverter implements GenericConverter<SegnoZodiacale, SegnoZodiacaleDto> {

    public SegnoZodiacale fromDToE(SegnoZodiacaleDto d) {
        SegnoZodiacale e = SegnoZodiacale.builder()
                .id(d.getId())
                .nome(d.getNome())
                .descrizione(d.getDescrizione())
                .build();

        if (d.getElementoId() != null) {
            Elemento elemento = new Elemento();
            elemento.setId(d.getElementoId());
            e.setElemento(elemento);
        }

        if (d.getPianetaId() != null) {
            Pianeta pianeta = new Pianeta();
            pianeta.setId(d.getPianetaId());
            e.setPianeta(pianeta);
        }

        if (d.getMetalloId() != null) {
            Metallo metallo = new Metallo();
            metallo.setId(d.getMetalloId());
            e.setMetallo(metallo);
        }

        return e;
    }

    public SegnoZodiacaleDto fromEToD(SegnoZodiacale e) {
        return SegnoZodiacaleDto.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descrizione(e.getDescrizione())
                .elementoId(e.getElemento() != null ? e.getElemento().getId() : null)
                .pianetaId(e.getPianeta() != null ? e.getPianeta().getId() : null)
                .metalloId(e.getMetallo() != null ? e.getMetallo().getId() : null)
                .build();
    }
}
