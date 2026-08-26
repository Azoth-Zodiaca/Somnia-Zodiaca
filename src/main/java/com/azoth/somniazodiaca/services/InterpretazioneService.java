package com.azoth.somniazodiaca.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.InterpretazioneConverter;
import com.azoth.somniazodiaca.dtos.RichiestaInterpretazioneDto;
import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.repositories.InterpretazioneRepository;

@Service
public class InterpretazioneService extends
        GenericService<Long, Interpretazione, RichiestaInterpretazioneDto, InterpretazioneConverter, InterpretazioneRepository> {

    public InterpretazioneService(InterpretazioneRepository repository, InterpretazioneConverter converter) {
        super(repository, converter);
    }

    public List<RichiestaInterpretazioneDto> findBySognoId(Long sognoId) {
        return getRepository().findBySognoId(sognoId).stream().map(getConverter()::fromEToD).toList();
    }

    public List<RichiestaInterpretazioneDto> findByUtenteId(Long utenteId) {
        return getRepository()
                .findDisponibiliByUtenteId(utenteId, LocalDateTime.now())
                .stream()
                .map(getConverter()::fromEToD)
                .toList();
    }

    public List<RichiestaInterpretazioneDto> findProssimeInScadenza(Long utenteId) {
        return getRepository()
                .findProssimeInScadenza(
                        utenteId,
                        LocalDateTime.now(),
                        PageRequest.of(0, 3))
                .stream()
                .map(getConverter()::fromEToD)
                .toList();
    }
}
