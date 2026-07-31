package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.UtenteConverter;
import com.azoth.somniazodiaca.dtos.CreazioneUtenteDto;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.repositories.UtenteRepository;

public class UtenteService extends GenericService<Long, Utente, CreazioneUtenteDto, UtenteConverter, UtenteRepository>{

    public UtenteService(UtenteRepository repository, UtenteConverter converter) {
        super(repository, converter);
    }


}
