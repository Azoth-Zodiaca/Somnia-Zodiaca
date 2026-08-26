package com.azoth.somniazodiaca.services;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.azoth.somniazodiaca.dtos.records.AstroWayChartRequest;
import com.azoth.somniazodiaca.dtos.records.AstroWayInterpretationRequest;
import com.azoth.somniazodiaca.exceptions.AstroWayUnavailableException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class AstroWayService {

    private final RestClient restClient;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public AstroWayService(
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            @Value("${astroway.base-url}") String baseUrl,
            @Value("${astroway.api-key}") String apiKey) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        this.restClient = restClientBuilder
                .requestFactory(requestFactory)
                .baseUrl(baseUrl)
                .build();

        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    public String getInterpretation(AstroWayInterpretationRequest request) {
        try {
            return restClient.post()
                    .uri("/v1/interpret/natal")
                    .header("X-Api-Key", apiKey)
                    .header("Accept-Language", "it")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);

        } catch (RestClientException exception) {
            throw new AstroWayUnavailableException(
                    "Impossibile raggiungere AstroWay per l'interpretazione del tema",
                    exception);
        }
    }

    public String calculateChart(AstroWayChartRequest request) {
        try {
            return restClient.post()
                    .uri("/v1/chart")
                    .header("X-Api-Key", apiKey)
                    .body(request)
                    .retrieve()
                    .body(String.class);

        } catch (ResourceAccessException exception) {
            throw new AstroWayUnavailableException(
                    "Impossibile raggiungere AstroWay", exception);
        }
    }

    public JsonNode parseChart(String rispostaAstroWay) {
        try {
            return objectMapper.readTree(rispostaAstroWay);
        } catch (Exception exception) {
            throw new AstroWayUnavailableException(
                    "Risposta AstroWay non valida", exception);
        }
    }
}