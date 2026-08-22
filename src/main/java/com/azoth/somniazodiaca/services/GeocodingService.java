package com.azoth.somniazodiaca.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.azoth.somniazodiaca.dtos.records.GeoNamesPlace;
import com.azoth.somniazodiaca.dtos.records.GeoNamesSearchResponse;
import com.azoth.somniazodiaca.dtos.records.LocalitaDto;
import com.azoth.somniazodiaca.exceptions.GeoNamesRateLimitException;
import com.azoth.somniazodiaca.exceptions.GeoNamesUnavailableException;

@Service
public class GeocodingService {

        private final RestClient restClient;
        private final String geonamesUsername;

        public GeocodingService(
                        RestClient.Builder restClientBuilder,
                        @Value("${geonames.base-url}") String baseUrl,
                        @Value("${geonames.username}") String geonamesUsername) {

                SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

                requestFactory.setConnectTimeout(Duration.ofSeconds(3));
                requestFactory.setReadTimeout(Duration.ofSeconds(5));

                this.restClient = restClientBuilder
                                .requestFactory(requestFactory)
                                .baseUrl(baseUrl)
                                .build();

                this.geonamesUsername = geonamesUsername;
        }

        public List<LocalitaDto> search(String query) {
                if (query == null || query.trim().length() < 3) {
                        return Collections.emptyList();
                }

                String normalizedQuery = query.trim();
                GeoNamesSearchResponse response;

                try {
                        response = restClient.get()
                                        .uri(uriBuilder -> uriBuilder
                                                        .path("/searchJSON")
                                                        .queryParam("q", normalizedQuery)
                                                        .queryParam("maxRows", 5)
                                                        .queryParam("featureClass", "P")
                                                        .queryParam("username", geonamesUsername)
                                                        .queryParam("style", "SHORT")
                                                        .build())
                                        .retrieve()
                                        .onStatus(
                                                        status -> status.value() == 429,
                                                        (request, responseError) -> {
                                                                throw new GeoNamesRateLimitException(
                                                                                "Limite richieste GeoNames raggiunto");
                                                        })
                                        .onStatus(
                                                        status -> status.is4xxClientError(),
                                                        (request, responseError) -> {
                                                                throw new GeoNamesUnavailableException(
                                                                                "Richiesta GeoNames non valida");
                                                        })
                                        .onStatus(
                                                        status -> status.is5xxServerError(),
                                                        (request, responseError) -> {
                                                                throw new GeoNamesUnavailableException(
                                                                                "GeoNames non disponibile");
                                                        })
                                        .body(GeoNamesSearchResponse.class);

                } catch (ResourceAccessException exception) {
                        throw new GeoNamesUnavailableException(
                                        "Impossibile raggiungere GeoNames, non ha risposto entro il timeout", exception);
                }

                if (response == null || response.geonames() == null) {
                        return Collections.emptyList();
                }

                return response.geonames()
                                .stream()
                                .map(this::toDto)
                                .toList();
        }

        private LocalitaDto toDto(GeoNamesPlace place) {
                if (place.lat() == null || place.lng() == null) {
                        throw new GeoNamesUnavailableException(
                                        "GeoNames ha restituito una località senza coordinate");
                }

                return new LocalitaDto(
                                place.geonameId(),
                                place.name(),
                                place.countryName(),
                                place.countryCode(),
                                new BigDecimal(place.lat()),
                                new BigDecimal(place.lng()),
                                place.timezone() != null
                                                ? place.timezone().timezoneId()
                                                : null);
        }
}