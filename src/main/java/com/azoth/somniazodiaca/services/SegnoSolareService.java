package com.azoth.somniazodiaca.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.enums.SegnoZodiacaleEnum;

@Service
public class SegnoSolareService {

    public SegnoZodiacaleEnum calcola(LocalDate dataNascita) {
        if (dataNascita == null) {
            throw new IllegalArgumentException(
                    "La data di nascita è obbligatoria");
        }

        int giorno = dataNascita.getDayOfMonth();
        int mese = dataNascita.getMonthValue();

        if ((mese == 3 && giorno >= 21) || (mese == 4 && giorno <= 19)) {
            return SegnoZodiacaleEnum.ARIETE;
        }

        if ((mese == 4 && giorno >= 20) || (mese == 5 && giorno <= 20)) {
            return SegnoZodiacaleEnum.TORO;
        }

        if ((mese == 5 && giorno >= 21) || (mese == 6 && giorno <= 20)) {
            return SegnoZodiacaleEnum.GEMELLI;
        }

        if ((mese == 6 && giorno >= 21) || (mese == 7 && giorno <= 22)) {
            return SegnoZodiacaleEnum.CANCRO;
        }

        if ((mese == 7 && giorno >= 23) || (mese == 8 && giorno <= 22)) {
            return SegnoZodiacaleEnum.LEONE;
        }

        if ((mese == 8 && giorno >= 23) || (mese == 9 && giorno <= 22)) {
            return SegnoZodiacaleEnum.VERGINE;
        }

        if ((mese == 9 && giorno >= 23) || (mese == 10 && giorno <= 22)) {
            return SegnoZodiacaleEnum.BILANCIA;
        }

        if ((mese == 10 && giorno >= 23) || (mese == 11 && giorno <= 21)) {
            return SegnoZodiacaleEnum.SCORPIONE;
        }

        if ((mese == 11 && giorno >= 22) || (mese == 12 && giorno <= 21)) {
            return SegnoZodiacaleEnum.SAGITTARIO;
        }

        if ((mese == 12 && giorno >= 22) || (mese == 1 && giorno <= 19)) {
            return SegnoZodiacaleEnum.CAPRICORNO;
        }

        if ((mese == 1 && giorno >= 20) || (mese == 2 && giorno <= 18)) {
            return SegnoZodiacaleEnum.ACQUARIO;
        }

        return SegnoZodiacaleEnum.PESCI;
    }
}