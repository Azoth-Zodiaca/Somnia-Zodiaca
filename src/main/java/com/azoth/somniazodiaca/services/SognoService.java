package com.azoth.somniazodiaca.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.SognoConverter;
import com.azoth.somniazodiaca.dtos.AggiuntaSognoDto;
import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.repositories.SognoRepository;

@Service
public class SognoService extends GenericService<Long, Sogno, AggiuntaSognoDto, SognoConverter, SognoRepository> {

    public SognoService(SognoRepository repository, SognoConverter converter) {
        super(repository, converter);
    }

    public List<AggiuntaSognoDto> findByUtenteId(Long utenteId) {
        return getRepository().findByUtenteId(utenteId).stream().map(getConverter()::fromEToD).toList();
    }

    public long countSogniDisponibiliByUtenteId(Long utenteId, java.time.LocalDateTime data) {
        return getRepository().countSogniDisponibiliByUtenteId(utenteId, data);
    }
}
