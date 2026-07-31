package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.InterpretazioneConverter;
import com.azoth.somniazodiaca.dtos.RichiestaInterpretazioneDto;
import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.repositories.InterpretazioneRepository;

public class InterpretazioneService extends GenericService<Long, Interpretazione, RichiestaInterpretazioneDto, InterpretazioneConverter, InterpretazioneRepository> {

    public InterpretazioneService(InterpretazioneRepository repository, InterpretazioneConverter converter) {
        super(repository, converter);
    }

}
