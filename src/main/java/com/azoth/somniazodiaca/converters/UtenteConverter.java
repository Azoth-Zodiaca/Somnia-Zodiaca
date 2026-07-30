package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.UtenteDto;
import com.azoth.somniazodiaca.entities.Utente;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtenteConverter implements GenericConverter<Utente, UtenteDto> {

    private final TemaNataleConverter temaNataleConverter;

    public Utente fromDToE(UtenteDto d) {

        Utente e = Utente.builder()
                .id(d.getId())
                .username(d.getUsername())
                .email(d.getEmail())
                .ruolo(d.getRuolo())
                .qi(d.getQi())
                .dataRegistrazione(d.getDataRegistrazione())
                .ultimoAccesso(d.getUltimoAccesso())
                .build();

        if (d.getTemaNatale() != null) {
            e.setTemaNatale(temaNataleConverter.fromDToE(d.getTemaNatale()));
        }

        return e;
    }

    public UtenteDto fromEToD(Utente e) {

        UtenteDto d = UtenteDto.builder()
                .id(e.getId())
                .username(e.getUsername())
                .email(e.getEmail())
                .ruolo(e.getRuolo())
                .qi(e.getQi())
                .dataRegistrazione(e.getDataRegistrazione())
                .ultimoAccesso(e.getUltimoAccesso())
                .build();

        if (e.getTemaNatale() != null) {
            d.setTemaNatale(temaNataleConverter.fromEToD(e.getTemaNatale()));
        }

        return d;
    }

}