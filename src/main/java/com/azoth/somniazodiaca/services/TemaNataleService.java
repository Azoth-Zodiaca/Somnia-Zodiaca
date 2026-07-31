package com.azoth.somniazodiaca.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.TemaNataleConverter;
import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.entities.TemaNatale;
import com.azoth.somniazodiaca.repositories.TemaNataleRepository;

@Service
public class TemaNataleService extends GenericService<Long, TemaNatale, TemaNataleDto, TemaNataleConverter, TemaNataleRepository> {

    public TemaNataleService(TemaNataleRepository repository, TemaNataleConverter converter) {
        super(repository, converter);
    }

    public Optional<TemaNataleDto> findByUtenteId(Long utenteId) {
        return getRepository().findByUtenteId(utenteId).map(getConverter()::fromEToD);
    }
}
