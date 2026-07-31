package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.InterpretazioneConverter;
import com.azoth.somniazodiaca.dtos.InterpretazioneDto;
import com.azoth.somniazodiaca.entities.Interpretazione;

public class InterpretazioneService extends GenericService<Long, Interpretazione, InterpretazioneDto, InterpretazioneConverter, InterpretazioneRepository> {

    public InterpretazioneService(InterpretazioneRepository repository, InterpretazioneConverter converter) {
        super(repository, converter);
    }

}
