package com.azoth.somniazodiaca.services;

public class ElementoService extends GenericService<Long, Elemento, ElementoDto, ElementoConverter, ElementoRepository> {

    public ElementoService(ElementoRepository repository, ElementoConverter converter) {
        super(repository, converter);
    }

}
