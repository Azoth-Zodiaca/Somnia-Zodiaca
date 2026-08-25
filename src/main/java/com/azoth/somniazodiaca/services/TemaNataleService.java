package com.azoth.somniazodiaca.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.TemaNataleConverter;
import com.azoth.somniazodiaca.dtos.TemaNataleDto;
import com.azoth.somniazodiaca.entities.SegnoZodiacale;
import com.azoth.somniazodiaca.entities.TemaNatale;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.Ruolo;
import com.azoth.somniazodiaca.enums.SegnoZodiacaleEnum;
import com.azoth.somniazodiaca.repositories.SegnoZodiacaleRepository;
import com.azoth.somniazodiaca.repositories.TemaNataleRepository;
import com.azoth.somniazodiaca.repositories.UtenteRepository;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.transaction.Transactional;

@Service
public class TemaNataleService
                extends GenericService<Long, TemaNatale, TemaNataleDto, TemaNataleConverter, TemaNataleRepository> {

        private final SegnoZodiacaleRepository segnoZodiacaleRepository;
        private final AstroWayService astroWayService;
        private final TemaNataleViewService temaNataleViewService;
        private final UtenteRepository utenteRepository;

        public TemaNataleService(
                        TemaNataleRepository repository,
                        TemaNataleConverter converter,
                        SegnoZodiacaleRepository segnoZodiacaleRepository,
                        AstroWayService astroWayService,
                        TemaNataleViewService temaNataleViewService, UtenteRepository utenteRepository) {

                super(repository, converter);
                this.segnoZodiacaleRepository = segnoZodiacaleRepository;
                this.astroWayService = astroWayService;
                this.temaNataleViewService = temaNataleViewService;
                this.utenteRepository = utenteRepository;
        }

        public Optional<TemaNataleDto> findByUtenteId(Long utenteId) {
                return getRepository().findByUtenteId(utenteId).map(getConverter()::fromEToD);
        }

        @Transactional
        public void creaTemaNatale(
                        Utente utente,
                        LocalDate dataNascita,
                        LocalTime oraNascita,
                        String luogoNascita,
                        Long geonameId,
                        BigDecimal latitudine,
                        BigDecimal longitudine,
                        String timezone,
                        String rispostaAstroWay) {

                // if (utente.getRuolo() != Ruolo.PREMIUM
                //                 && utente.getRuolo() != Ruolo.ADMIN) {
                //         throw new IllegalStateException(
                //                         "Il calcolo del tema natale richiede Premium");
                // }

                TemaNatale temaNatale = getRepository()
                                .findByUtenteId(utente.getId())
                                .orElseGet(TemaNatale::new);

                temaNatale.setUtente(utente);
                temaNatale.setDataNascita(dataNascita);
                temaNatale.setOraNascita(oraNascita);
                temaNatale.setLuogoNascita(luogoNascita);
                temaNatale.setGeonameId(geonameId);
                temaNatale.setLatitudine(latitudine);
                temaNatale.setLongitudine(longitudine);
                temaNatale.setTimezone(timezone);
                temaNatale.setRispostaAstroWay(rispostaAstroWay);
                temaNatale.setDataCreazione(LocalDateTime.now());

                JsonNode risposta = astroWayService.parseChart(rispostaAstroWay);

                if (!risposta.path("ok").asBoolean(false)) {
                        throw new IllegalStateException(
                                        "AstroWay ha restituito una risposta non valida");
                }

                JsonNode datiTema = risposta.path("data");

                SegnoZodiacaleEnum segnoSolare = segnoSole(datiTema);
                SegnoZodiacaleEnum ascendente = segnoAscendente(datiTema);

                SegnoZodiacale segnoSolareEntity = segnoZodiacaleRepository
                                .findBySegnoZodiacale(segnoSolare)
                                .orElseThrow(() -> new IllegalStateException(
                                                "Segno solare non trovato nel database"));

                SegnoZodiacale ascendenteEntity = segnoZodiacaleRepository
                                .findBySegnoZodiacale(ascendente)
                                .orElseThrow(() -> new IllegalStateException(
                                                "Ascendente non trovato nel database"));

                utente.setSegnoZodiacale(segnoSolareEntity);
                utente.setAscendente(ascendenteEntity);

                double longitudineSole = datiTema.path("planets")
                                .elements()
                                .next()
                                .path("longitude")
                                .asDouble();

                getRepository().save(temaNatale);
                utenteRepository.save(utente);
        }

        private SegnoZodiacaleEnum segnoSole(JsonNode datiTema) {
                for (JsonNode pianeta : datiTema.path("planets")) {
                        if ("Sun".equals(pianeta.path("name").asText())) {
                                String segno = temaNataleViewService.segnoDaLongitudine(
                                                pianeta.path("longitude").asDouble());

                                return SegnoZodiacaleEnum.valueOf(
                                                segno.toUpperCase());
                        }
                }

                throw new IllegalStateException(
                                "Sole non presente nella risposta AstroWay");
        }

        private SegnoZodiacaleEnum segnoAscendente(JsonNode datiTema) {
                double longitudineAscendente = datiTema
                                .path("houses")
                                .path("ascendant")
                                .asDouble();

                String segno = temaNataleViewService.segnoDaLongitudine(
                                longitudineAscendente);

                return SegnoZodiacaleEnum.valueOf(segno.toUpperCase());
        }
}
