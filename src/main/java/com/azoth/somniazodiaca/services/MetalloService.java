package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.MetalloConverter;
import com.azoth.somniazodiaca.dtos.MetalloDto;
import com.azoth.somniazodiaca.entities.Metallo;

public class MetalloService extends GenericService<Long, Metallo, MetalloDto, MetalloConverter, MetalloRepository> {

    public MetalloService(MetalloRepository repository, MetalloConverter converter) {
        super(repository, converter);
    }

}
