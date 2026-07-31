package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.UtenteDetail;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.entities.SegnoZodiacale;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtenteConverter implements GenericConverter<Utente, UtenteDetail> {

    private final TemaNataleConverter temaNataleConverter;
    private final SegnoZodiacaleConverter segnoZodiacaleConverter;

    public Utente fromDToE(UtenteDetail d) {

        Utente e = Utente.builder()
                .id(d.getId())
                .username(d.getUsername())
                .email(d.getEmail())
                .ruolo(d.getRuolo())
                .qi(d.getQi())
                .dataRegistrazione(d.getDataRegistrazione())
                .ultimoAccesso(d.getUltimoAccesso())
                .build();

        if (d.getSegnoZodiacale() != null) {
            e.setSegnoZodiacale(segnoZodiacaleConverter.fromDToE(d.getSegnoZodiacale()));
        }

        if (d.getAscendente() != null) {
            e.setAscendente(segnoZodiacaleConverter.fromDToE(d.getAscendente()));
        }

        if (d.getTemaNatale() != null) {
            e.setTemaNatale(temaNataleConverter.fromDToE(d.getTemaNatale()));
        }

        return e;
    }

    public UtenteDetail fromEToD(Utente e) {

        UtenteDetail d = UtenteDetail.builder()
                .id(e.getId())
                .username(e.getUsername())
                .email(e.getEmail())
                .ruolo(e.getRuolo())
                .qi(e.getQi())
                .dataRegistrazione(e.getDataRegistrazione())
                .ultimoAccesso(e.getUltimoAccesso())
                .build();

        if (e.getSegnoZodiacale() != null) {
            d.setSegnoZodiacale(segnoZodiacaleConverter.fromEToD(e.getSegnoZodiacale()));
        }

        if (e.getAscendente() != null) {
            d.setAscendente(segnoZodiacaleConverter.fromEToD(e.getAscendente()));
        }

        if (e.getTemaNatale() != null) {
            d.setTemaNatale(temaNataleConverter.fromEToD(e.getTemaNatale()));
        }

        return d;
    }
}