package com.azoth.somniazodiaca.services;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.MetalloConverter;
import com.azoth.somniazodiaca.dtos.MetalloDto;
import com.azoth.somniazodiaca.entities.Metallo;
import com.azoth.somniazodiaca.repositories.MetalloRepository;

@Service
public class MetalloService extends GenericService<Long, Metallo, MetalloDto, MetalloConverter, MetalloRepository> {

    public MetalloService(MetalloRepository repository, MetalloConverter converter) {
        super(repository, converter);
    }

}
