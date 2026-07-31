package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.ElementoConverter;
import com.azoth.somniazodiaca.dtos.ElementoDto;
import com.azoth.somniazodiaca.entities.Elemento;
import com.azoth.somniazodiaca.repositories.ElementoRepository;

public class ElementoService extends GenericService<Long, Elemento, ElementoDto, ElementoConverter, ElementoRepository> {

    public ElementoService(ElementoRepository repository, ElementoConverter converter) {
        super(repository, converter);
    }

}
