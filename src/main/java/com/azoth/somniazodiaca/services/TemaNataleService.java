package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.TemaNataleConverter;
import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.entities.TemaNatale;
import com.azoth.somniazodiaca.repositories.TemaNataleRepository;

public class TemaNataleService extends GenericService<Long, TemaNatale, TemaNataleDto, TemaNataleConverter, TemaNataleRepository> {

    public TemaNataleService(TemaNataleRepository repository, TemaNataleConverter converter) {
        super(repository, converter);
    }

}
