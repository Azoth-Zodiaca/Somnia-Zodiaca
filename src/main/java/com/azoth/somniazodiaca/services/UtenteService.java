package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.UtenteConverter;
import com.azoth.somniazodiaca.dtos.UtenteDto;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

public class UtenteService extends GenericService<Long, Utente, UtenteDto, UtenteConverter, UtenteRepository>{

    public UtenteService(UtenteRepository repository, UtenteConverter converter) {
        super(repository, converter);
    }


}
