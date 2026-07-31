package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.PianetaConverter;
import com.azoth.somniazodiaca.dtos.PianetaDto;
import com.azoth.somniazodiaca.entities.Pianeta;

public class PianetaService extends GenericService<Long, Pianeta, PianetaDto, PianetaConverter, PianetaRepository> {

    public PianetaService(PianetaRepository repository, PianetaConverter converter) {
        super(repository, converter);
    }

}
