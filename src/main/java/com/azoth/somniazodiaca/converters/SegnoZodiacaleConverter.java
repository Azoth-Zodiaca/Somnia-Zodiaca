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
                .segnoZodiacale(d.getSegnoZodiacale())
                .modalita(d.getModalita())
                .descrizione(d.getDescrizione())
                .build();

        if (d.getElementoId() != null) {
            Elemento elemento = Elemento.builder().id(d.getElementoId()).build();
            e.setElemento(elemento);
        }
 
        if (d.getPianetaId() != null) {
            Pianeta pianeta = Pianeta.builder().id(d.getPianetaId()).build();
            e.setPianeta(pianeta);
        }
 
        if (d.getMetalloId() != null) {
            Metallo metallo = Metallo.builder().id(d.getMetalloId()).build();
            e.setMetallo(metallo);
        }

        return e;
    }

    public SegnoZodiacaleDto fromEToD(SegnoZodiacale e) {
        return SegnoZodiacaleDto.builder()
                .id(e.getId())
                .segnoZodiacale(e.getSegnoZodiacale())
                .modalita(e.getModalita())
                .descrizione(e.getDescrizione())
                .elementoId(e.getElemento() != null ? e.getElemento().getId() : null)
                .pianetaId(e.getPianeta() != null ? e.getPianeta().getId() : null)
                .metalloId(e.getMetallo() != null ? e.getMetallo().getId() : null)
                .build();
    }
}
