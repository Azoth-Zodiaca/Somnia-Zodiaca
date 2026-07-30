package com.azoth.somniazodiaca.converters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.CosmeticoDto;
import com.azoth.somniazodiaca.entities.Cosmetico;
import com.azoth.somniazodiaca.entities.InventarioCosmetico;

@Service
public class CosmeticoConverter implements GenericConverter<Cosmetico, CosmeticoDto> {

    public Cosmetico fromDToE(CosmeticoDto d) {
        Cosmetico e = Cosmetico.builder()
                .id(d.getId())
                .nome(d.getNome())
                .descrizione(d.getDescrizione())
                .prezzoQi(d.getPrezzoQi())
                .build();

        if (d.getInventariIds() != null) {
            Set<InventarioCosmetico> inventari = d.getInventariIds().stream()
                    .map(id -> {
                        InventarioCosmetico inv = new InventarioCosmetico();
                        inv.setId(id);
                        return inv;
                    })
                    .collect(Collectors.toSet());
            e.setInventari(inventari);
        }

        return e;
    }

    public CosmeticoDto fromEToD(Cosmetico e) {
        CosmeticoDto d = CosmeticoDto.builder()
                .id(e.getId())
                .nome(e.getNome())
                .descrizione(e.getDescrizione())
                .prezzoQi(e.getPrezzoQi())
                .build();

        if (e.getInventari() != null) {
            d.setInventariIds(e.getInventari().stream()
                    .map(InventarioCosmetico::getId)
                    .collect(Collectors.toList()));
        }

        return d;
    }
}
