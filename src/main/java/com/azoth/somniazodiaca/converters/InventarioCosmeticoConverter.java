package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.AggiuntaInventarioCosmeticoDto;
import com.azoth.somniazodiaca.entities.Cosmetico;
import com.azoth.somniazodiaca.entities.InventarioCosmetico;
import com.azoth.somniazodiaca.entities.Utente;

@Service
public class InventarioCosmeticoConverter implements GenericConverter<InventarioCosmetico, AggiuntaInventarioCosmeticoDto> {

    public InventarioCosmetico fromDToE(AggiuntaInventarioCosmeticoDto d) {
        InventarioCosmetico e = InventarioCosmetico.builder()
                .id(d.getId())
                .equipaggiato(d.getEquipaggiato())
                .dataAcquisto(d.getDataAcquisto())
                .build();

        if (d.getUtenteId() != null) {
            Utente utente = new Utente();
            utente.setId(d.getUtenteId());
            e.setUtente(utente);
        }

        if (d.getCosmeticoId() != null) {
            Cosmetico cosmetico = new Cosmetico();
            cosmetico.setId(d.getCosmeticoId());
            e.setCosmetico(cosmetico);
        }

        return e;
    }

    public AggiuntaInventarioCosmeticoDto fromEToD(InventarioCosmetico e) {
        return AggiuntaInventarioCosmeticoDto.builder()
                .id(e.getId())
                .utenteId(e.getUtente() != null ? e.getUtente().getId() : null)
                .cosmeticoId(e.getCosmetico() != null ? e.getCosmetico().getId() : null)
                .equipaggiato(e.getEquipaggiato())
                .dataAcquisto(e.getDataAcquisto())
                .build();
    }
}
