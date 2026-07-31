package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.entities.TemaNatale;
import com.azoth.somniazodiaca.entities.Utente;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemaNataleConverter implements GenericConverter<TemaNatale, TemaNataleDto> {

    public TemaNatale fromDToE(TemaNataleDto d) {

        TemaNatale temaNatale = TemaNatale.builder()
                .id(d.getId())
                .dataNascita(d.getDataNascita())
                .oraNascita(d.getOraNascita())
                .luogoNascita(d.getLuogoNascita())
                .latitudine(d.getLatitudine())
                .longitudine(d.getLongitudine())
                .timezone(d.getTimezone())
                .dataCreazione(d.getDataCreazione())
                .build();

        if (d.getUtenteId() != null) {
            Utente utente = Utente.builder().id(d.getUtenteId()).build();
            temaNatale.setUtente(utente);
        }

        return temaNatale;
    }

    public TemaNataleDto fromEToD(TemaNatale e) {

        return TemaNataleDto.builder()
                .id(e.getId())
                .utenteId(e.getUtente() != null ? e.getUtente().getId() : null)
                .dataNascita(e.getDataNascita())
                .oraNascita(e.getOraNascita())
                .luogoNascita(e.getLuogoNascita())
                .latitudine(e.getLatitudine())
                .longitudine(e.getLongitudine())
                .timezone(e.getTimezone())
                .dataCreazione(e.getDataCreazione())
                .build();
    }

}