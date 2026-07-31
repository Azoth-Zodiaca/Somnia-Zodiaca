package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.SegnoZodiacaleConverter;
import com.azoth.somniazodiaca.dtos.SegnoZodiacaleDto;
import com.azoth.somniazodiaca.entities.SegnoZodiacale;
import com.azoth.somniazodiaca.repositories.SegnoZodiacaleRepository;

public class SegnoZodiacaleService extends GenericService<Long, SegnoZodiacale, SegnoZodiacaleDto, SegnoZodiacaleConverter, SegnoZodiacaleRepository> {

    public SegnoZodiacaleService(SegnoZodiacaleRepository repository, SegnoZodiacaleConverter converter) {
        super(repository, converter);
    }

}
