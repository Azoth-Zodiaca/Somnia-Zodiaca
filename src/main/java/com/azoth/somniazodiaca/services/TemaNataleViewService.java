package com.azoth.somniazodiaca.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.records.PianetaTemaView;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class TemaNataleViewService {

    private static final String[] NOMI_SEGNI = {
            "Ariete",
            "Toro",
            "Gemelli",
            "Cancro",
            "Leone",
            "Vergine",
            "Bilancia",
            "Scorpione",
            "Sagittario",
            "Capricorno",
            "Acquario",
            "Pesci"
    };

    public List<PianetaTemaView> estraiPianeti(JsonNode chart) {
        JsonNode pianeti = chart.path("planets");
        JsonNode houses = chart.path("houses");

        if (!pianeti.isArray()) {
            return List.of();
        }

        return java.util.stream.StreamSupport
                .stream(pianeti.spliterator(), false)
                .map(p -> convertiPianeta(p, houses))
                .toList();

    }

    private PianetaTemaView convertiPianeta(JsonNode pianeta, JsonNode houses) {
        double longitudine = pianeta.path("longitude").asDouble();
        int indiceSegno = (int) (longitudine / 30.0);

        String segno = NOMI_SEGNI[indiceSegno];
        int casa = casaDi(longitudine, houses);

        return new PianetaTemaView(
                nomeItaliano(pianeta.path("name").asText()),
                segno,
                longitudine,
                pianeta.path("isRetrograde").asBoolean(false),
                elementoDi(segno),
                modalitaDi(segno),
                casa);
    }

    private String nomeItaliano(String nome) {
        return switch (nome) {
            case "Sun" -> "Sole";
            case "Moon" -> "Luna";
            case "Mercury" -> "Mercurio";
            case "Venus" -> "Venere";
            case "Mars" -> "Marte";
            case "Jupiter" -> "Giove";
            case "Saturn" -> "Saturno";
            case "Uranus" -> "Urano";
            case "Neptune" -> "Nettuno";
            case "Pluto" -> "Plutone";
            case "true Node" -> "Nodo Nord";
            case "mean Apogee" -> "Lilith";
            case "Chiron" -> "Chirone";
            default -> nome;
        };
    }

    private String elementoDi(String segno) {
        return switch (segno) {
            case "Ariete", "Leone", "Sagittario" -> "Fuoco";
            case "Toro", "Vergine", "Capricorno" -> "Terra";
            case "Gemelli", "Bilancia", "Acquario" -> "Aria";
            case "Cancro", "Scorpione", "Pesci" -> "Acqua";
            default -> throw new IllegalArgumentException(
                    "Segno sconosciuto: " + segno);
        };
    }

    private String modalitaDi(String segno) {
        return switch (segno) {
            case "Ariete", "Cancro", "Bilancia", "Capricorno" -> "Cardinale";
            case "Toro", "Leone", "Scorpione", "Acquario" -> "Fisso";
            case "Gemelli", "Vergine", "Sagittario", "Pesci" -> "Mobile";
            default -> throw new IllegalArgumentException(
                    "Segno sconosciuto: " + segno);
        };
    }

    private int casaDi(double longitudine, JsonNode houses) {
        double[] cuspidi = new double[12];

        for (int i = 0; i < 12; i++) {
            cuspidi[i] = houses.path("house" + (i + 1)).asDouble();
        }

        for (int i = 0; i < 12; i++) {
            double cuspide = cuspidi[i];
            double next = cuspidi[(i + 1) % 12];

            boolean inRange;

            if (cuspide < next) {
                inRange = longitudine >= cuspide && longitudine < next;
            } else {
                inRange = longitudine >= cuspide || longitudine < next;
            }

            if (inRange) {
                return i + 1;
            }
        }

        return 1;
    }

}