package com.azoth.somniazodiaca.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.InventarioCosmeticoConverter;
import com.azoth.somniazodiaca.dtos.AggiuntaInventarioCosmeticoDto;
import com.azoth.somniazodiaca.dtos.ModificaInventarioCosmeticoDto;
import com.azoth.somniazodiaca.entities.InventarioCosmetico;
import com.azoth.somniazodiaca.repositories.InventarioCosmeticoRepository;

@Service
public class InventarioCosmeticoService extends GenericService<Long, InventarioCosmetico, AggiuntaInventarioCosmeticoDto, InventarioCosmeticoConverter, InventarioCosmeticoRepository> {

    public InventarioCosmeticoService(InventarioCosmeticoRepository repository, InventarioCosmeticoConverter converter) {
        super(repository, converter);
    }

    public List<AggiuntaInventarioCosmeticoDto> findByUtenteId(Long utenteId) {
        return getRepository().findByUtenteId(utenteId).stream().map(getConverter()::fromEToD).toList();
    }

    public boolean setEquipaggiato(ModificaInventarioCosmeticoDto dto) {
        return getRepository().findByUtenteIdAndCosmeticoId(dto.getUtenteId(), dto.getCosmeticoId())
                .map(item -> {
                    item.setEquipaggiato(dto.getEquipaggiato());
                    getRepository().save(item);
                    return true;
                })
                .orElse(false);
    }
}
