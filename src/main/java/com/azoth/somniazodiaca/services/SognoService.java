package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.SognoConverter;
import com.azoth.somniazodiaca.dtos.AggiuntaSognoDto;
import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.repositories.SognoRepository;

public class SognoService extends GenericService<Long, Sogno, AggiuntaSognoDto, SognoConverter, SognoRepository> {

    public SognoService(SognoRepository repository, SognoConverter converter) {
        super(repository, converter);
    }

}
