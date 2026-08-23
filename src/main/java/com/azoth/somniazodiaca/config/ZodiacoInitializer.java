package com.azoth.somniazodiaca.config;

import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azoth.somniazodiaca.entities.Elemento;
import com.azoth.somniazodiaca.entities.Metallo;
import com.azoth.somniazodiaca.entities.Pianeta;
import com.azoth.somniazodiaca.entities.SegnoZodiacale;
import com.azoth.somniazodiaca.enums.ElementoEnum;
import com.azoth.somniazodiaca.enums.MetalloEnum;
import com.azoth.somniazodiaca.enums.Modalita;
import com.azoth.somniazodiaca.enums.PianetaEnum;
import com.azoth.somniazodiaca.enums.SegnoZodiacaleEnum;
import com.azoth.somniazodiaca.repositories.ElementoRepository;
import com.azoth.somniazodiaca.repositories.MetalloRepository;
import com.azoth.somniazodiaca.repositories.PianetaRepository;
import com.azoth.somniazodiaca.repositories.SegnoZodiacaleRepository;

@Configuration
public class ZodiacoInitializer {

    @Bean
    public CommandLineRunner initZodiaco(
            ElementoRepository elementoRepository,
            PianetaRepository pianetaRepository,
            MetalloRepository metalloRepository,
            SegnoZodiacaleRepository segnoRepository) {

        return args -> {
            Map<ElementoEnum, Elemento> elementi = creaElementi(elementoRepository);
            Map<PianetaEnum, Pianeta> pianeti = creaPianeti(pianetaRepository);
            Map<MetalloEnum, Metallo> metalli = creaMetalli(metalloRepository);

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.ARIETE,
                    Modalita.CARDINALE,
                    elementi.get(ElementoEnum.FUOCO),
                    pianeti.get(PianetaEnum.MARTE),
                    metalli.get(MetalloEnum.FERRO));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.TORO,
                    Modalita.FISSO,
                    elementi.get(ElementoEnum.TERRA),
                    pianeti.get(PianetaEnum.VENERE),
                    metalli.get(MetalloEnum.RAME));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.GEMELLI,
                    Modalita.MOBILE,
                    elementi.get(ElementoEnum.ARIA),
                    pianeti.get(PianetaEnum.MERCURIO),
                    metalli.get(MetalloEnum.MERCURIO));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.CANCRO,
                    Modalita.CARDINALE,
                    elementi.get(ElementoEnum.ACQUA),
                    pianeti.get(PianetaEnum.LUNA),
                    metalli.get(MetalloEnum.ARGENTO));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.LEONE,
                    Modalita.FISSO,
                    elementi.get(ElementoEnum.FUOCO),
                    pianeti.get(PianetaEnum.SOLE),
                    metalli.get(MetalloEnum.ORO));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.VERGINE,
                    Modalita.MOBILE,
                    elementi.get(ElementoEnum.TERRA),
                    pianeti.get(PianetaEnum.MERCURIO),
                    metalli.get(MetalloEnum.MERCURIO));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.BILANCIA,
                    Modalita.CARDINALE,
                    elementi.get(ElementoEnum.ARIA),
                    pianeti.get(PianetaEnum.VENERE),
                    metalli.get(MetalloEnum.RAME));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.SCORPIONE,
                    Modalita.FISSO,
                    elementi.get(ElementoEnum.ACQUA),
                    pianeti.get(PianetaEnum.PLUTONE),
                    metalli.get(MetalloEnum.FERRO));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.SAGITTARIO,
                    Modalita.MOBILE,
                    elementi.get(ElementoEnum.FUOCO),
                    pianeti.get(PianetaEnum.GIOVE),
                    metalli.get(MetalloEnum.STAGNO));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.CAPRICORNO,
                    Modalita.CARDINALE,
                    elementi.get(ElementoEnum.TERRA),
                    pianeti.get(PianetaEnum.SATURNO),
                    metalli.get(MetalloEnum.PIOMBO));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.ACQUARIO,
                    Modalita.FISSO,
                    elementi.get(ElementoEnum.ARIA),
                    pianeti.get(PianetaEnum.URANO),
                    metalli.get(MetalloEnum.PIOMBO));

            creaSegno(
                    segnoRepository,
                    SegnoZodiacaleEnum.PESCI,
                    Modalita.MOBILE,
                    elementi.get(ElementoEnum.ACQUA),
                    pianeti.get(PianetaEnum.NETTUNO),
                    metalli.get(MetalloEnum.STAGNO));

            System.out.println("Elementi, pianeti, metalli e segni zodiacali verificati.");
        };
    }

    private Map<ElementoEnum, Elemento> creaElementi(
            ElementoRepository repository) {

        for (ElementoEnum valore : ElementoEnum.values()) {
            if (repository.findByElemento(valore).isEmpty()) {
                repository.save(Elemento.builder()
                        .elemento(valore)
                        .descrizione("Elemento " + valore.name().toLowerCase())
                        .build());
            }
        }

        return repository.findAll()
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        Elemento::getElemento,
                        elemento -> elemento));
    }

    private Map<PianetaEnum, Pianeta> creaPianeti(
            PianetaRepository repository) {

        for (PianetaEnum valore : PianetaEnum.values()) {
            if (repository.findByPianeta(valore).isEmpty()) {
                repository.save(Pianeta.builder()
                        .pianeta(valore)
                        .descrizione("Pianeta " + valore.name().toLowerCase())
                        .build());
            }
        }

        return repository.findAll()
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        Pianeta::getPianeta,
                        pianeta -> pianeta));
    }

    private Map<MetalloEnum, Metallo> creaMetalli(
            MetalloRepository repository) {

        for (MetalloEnum valore : MetalloEnum.values()) {
            if (repository.findByMetallo(valore).isEmpty()) {
                repository.save(Metallo.builder()
                        .metallo(valore)
                        .descrizione("Metallo " + valore.name().toLowerCase())
                        .build());
            }
        }

        return repository.findAll()
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        Metallo::getMetallo,
                        metallo -> metallo));
    }

    private void creaSegno(
            SegnoZodiacaleRepository repository,
            SegnoZodiacaleEnum segno,
            Modalita modalita,
            Elemento elemento,
            Pianeta pianeta,
            Metallo metallo) {

        if (repository.findBySegnoZodiacale(segno).isEmpty()) {
            repository.save(SegnoZodiacale.builder()
                    .segnoZodiacale(segno)
                    .modalita(modalita)
                    .descrizione("Segno zodiacale " + segno.name().toLowerCase())
                    .elemento(elemento)
                    .pianeta(pianeta)
                    .metallo(metallo)
                    .build());
        }
    }
}