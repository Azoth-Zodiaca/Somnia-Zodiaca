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

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.azoth.somniazodiaca.dtos.records.AstroWayInterpretationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class TemaNataleService
                extends GenericService<Long, TemaNatale, TemaNataleDto, TemaNataleConverter, TemaNataleRepository> {

        private final SegnoZodiacaleRepository segnoZodiacaleRepository;
        private final AstroWayService astroWayService;
        private final TemaNataleViewService temaNataleViewService;
        private final UtenteRepository utenteRepository;
        private final GeminiService geminiService;
        private final ObjectMapper objectMapper;

        public TemaNataleService(
                        TemaNataleRepository repository,
                        TemaNataleConverter converter,
                        SegnoZodiacaleRepository segnoZodiacaleRepository,
                        AstroWayService astroWayService,
                        TemaNataleViewService temaNataleViewService, UtenteRepository utenteRepository,
                        GeminiService geminiService, ObjectMapper objectMapper) {

                super(repository, converter);
                this.segnoZodiacaleRepository = segnoZodiacaleRepository;
                this.astroWayService = astroWayService;
                this.temaNataleViewService = temaNataleViewService;
                this.utenteRepository = utenteRepository;
                this.geminiService = geminiService;
                this.objectMapper = objectMapper;
        }

        public Optional<TemaNataleDto> findByUtenteId(Long utenteId) {
                return getRepository().findByUtenteId(utenteId).map(getConverter()::fromEToD);
        }

        @Transactional
        public void generaInterpretazione(Utente utente) {
                if (utente.getRuolo() != Ruolo.PREMIUM && utente.getRuolo() != Ruolo.ADMIN) {
                        throw new IllegalStateException("L'interpretazione del tema natale richiede Premium");
                }

                TemaNatale temaNatale = getRepository().findByUtenteId(utente.getId())
                                .orElseThrow(() -> new IllegalStateException("Tema natale non trovato"));

                if (temaNatale.getDataNascita() == null
                                || temaNatale.getLatitudine() == null
                                || temaNatale.getLongitudine() == null
                                || temaNatale.getTimezone() == null) {
                        throw new IllegalStateException(
                                        "Dati di nascita insufficienti per l'interpretazione");
                }

                LocalTime oraNascita = temaNatale.getOraNascita() != null
                                ? temaNatale.getOraNascita()
                                : LocalTime.NOON;

                ZoneId zoneId = ZoneId.of(temaNatale.getTimezone());

                LocalDateTime dataOraLocale = LocalDateTime.of(
                                temaNatale.getDataNascita(),
                                oraNascita);

                ZoneOffset offset = zoneId.getRules().getOffset(dataOraLocale);

                double timezoneOffset = offset.getTotalSeconds() / 3600.0;

                AstroWayInterpretationRequest richiesta = new AstroWayInterpretationRequest(
                                temaNatale.getDataNascita().toString(),
                                oraNascita.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                timezoneOffset,
                                temaNatale.getLatitudine().doubleValue(),
                                temaNatale.getLongitudine().doubleValue(),
                                "P",
                                "it");

                String risposta = astroWayService.getInterpretation(richiesta);
                JsonNode json = astroWayService.parseChart(risposta);
                String testo = estraiInterpretazione(json);

                if (testo == null || testo.isBlank()) {
                        throw new IllegalStateException("AstroWay non ha restituito un'interpretazione valida");
                }

                temaNatale.setInterpretazioneAstroWay(testo);
                String analisi = geminiService.interpretNatalChart(
                                temaNatale.getRispostaAstroWay(), testo);
                temaNatale.setAnalisiGemini(normalizzaJson(analisi));
                getRepository().save(temaNatale);
        }

        private String normalizzaJson(String risposta) {
                try {
                        String json = risposta.trim();
                        if (json.startsWith("```") && json.endsWith("```")) {
                                json = json.substring(json.indexOf('\n') + 1, json.length() - 3).trim();
                        }
                        objectMapper.readTree(json);
                        return json;
                } catch (Exception exception) {
                        throw new IllegalStateException(
                                        "Gemini non ha restituito un'analisi JSON valida", exception);
                }
        }

        private String estraiInterpretazione(JsonNode risposta) {
                if (risposta.isTextual()) {
                        return risposta.asText();
                }

                String[] possibiliCampi = { "interpretation", "interpretazione", "text", "content", "narrative" };
                for (String campo : possibiliCampi) {
                        JsonNode valore = risposta.path(campo);
                        if (valore.isTextual() && !valore.asText().isBlank()) {
                                return valore.asText();
                        }
                }

                JsonNode data = risposta.path("data");
                if (!data.isMissingNode()) {
                        return estraiInterpretazione(data);
                }

                return null;
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
                // && utente.getRuolo() != Ruolo.ADMIN) {
                // throw new IllegalStateException(
                // "Il calcolo del tema natale richiede Premium");
                // }

                Optional<TemaNatale> temaEsistente = getRepository()
                                .findByUtenteId(utente.getId());

                if (temaEsistente.isPresent()
                                && utente.getRuolo() != Ruolo.PREMIUM
                                && utente.getRuolo() != Ruolo.ADMIN) {
                        throw new IllegalStateException(
                                        "La modifica del tema natale richiede Premium");
                }

                TemaNatale temaNatale = temaEsistente.orElseGet(TemaNatale::new);

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
