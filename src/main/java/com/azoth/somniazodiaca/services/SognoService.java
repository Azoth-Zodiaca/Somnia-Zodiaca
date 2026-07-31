package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.SognoConverter;
import com.azoth.somniazodiaca.dtos.SognoDto;
import com.azoth.somniazodiaca.entities.Sogno;
import com.azoth.somniazodiaca.repositories.SognoRepository;

public class SognoService extends GenericService<Long, Sogno, SognoDto, SognoConverter, SognoRepository> {

    public SognoService(SognoRepository repository, SognoConverter converter) {
        super(repository, converter);
    }

}
