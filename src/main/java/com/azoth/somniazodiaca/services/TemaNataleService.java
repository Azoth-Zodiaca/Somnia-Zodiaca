package com.azoth.somniazodiaca.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.TemaNataleConverter;
import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.entities.TemaNatale;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.repositories.TemaNataleRepository;

import jakarta.transaction.Transactional;

@Service
public class TemaNataleService
        extends GenericService<Long, TemaNatale, TemaNataleDto, TemaNataleConverter, TemaNataleRepository> {

    public TemaNataleService(TemaNataleRepository repository, TemaNataleConverter converter) {
        super(repository, converter);
    }

    public Optional<TemaNataleDto> findByUtenteId(Long utenteId) {
        return getRepository().findByUtenteId(utenteId).map(getConverter()::fromEToD);
    }

    @Transactional
    public void creaTemaNatale(
            Utente utente,
            LocalDate dataNascita,
            LocalTime oraNascita,
            String luogoNascita,
            Long geonameId,
            BigDecimal latitudine,
            BigDecimal longitudine,
            String timezone,
            String rispostaAstroWay) {

        TemaNatale temaNatale = getRepository()
                .findByUtenteId(utente.getId())
                .orElseGet(TemaNatale::new);

        temaNatale.setUtente(utente);
        temaNatale.setDataNascita(dataNascita);
        temaNatale.setOraNascita(oraNascita);
        temaNatale.setLuogoNascita(luogoNascita);
        temaNatale.setGeonameId(geonameId);
        temaNatale.setLatitudine(latitudine);
        temaNatale.setLongitudine(longitudine);
        temaNatale.setTimezone(timezone);
        temaNatale.setRispostaAstroWay(rispostaAstroWay);
        temaNatale.setDataCreazione(LocalDateTime.now());

        getRepository().save(temaNatale);
    }
}
