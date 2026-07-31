package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.CosmeticoConverter;
import com.azoth.somniazodiaca.dtos.CosmeticoDto;
import com.azoth.somniazodiaca.entities.Cosmetico;
import com.azoth.somniazodiaca.repositories.CosmeticoRepository;

public class CosmeticoService extends GenericService<Long, Cosmetico, CosmeticoDto, CosmeticoConverter, CosmeticoRepository> {

    public CosmeticoService(CosmeticoRepository repository, CosmeticoConverter converter) {
        super(repository, converter);
    }

}
