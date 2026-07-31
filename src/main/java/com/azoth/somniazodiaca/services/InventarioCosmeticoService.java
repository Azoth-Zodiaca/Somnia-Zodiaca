package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.InventarioCosmeticoConverter;
import com.azoth.somniazodiaca.dtos.InventarioCosmeticoDto;
import com.azoth.somniazodiaca.entities.InventarioCosmetico;
import com.azoth.somniazodiaca.repositories.InventarioCosmeticoRepository;

public class InventarioCosmeticoService extends GenericService<Long, InventarioCosmetico, InventarioCosmeticoDto, InventarioCosmeticoConverter, InventarioCosmeticoRepository> {

    public InventarioCosmeticoService(InventarioCosmeticoRepository repository, InventarioCosmeticoConverter converter) {
        super(repository, converter);
    }

}
