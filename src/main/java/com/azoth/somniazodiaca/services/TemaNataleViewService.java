package com.azoth.somniazodiaca.services;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.records.PercentualeTemaView;
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

        String segno = segnoDaLongitudine(longitudine);
        int casa = casaDi(longitudine, houses);

        String nome = nomeItaliano(pianeta.path("name").asText());

        return new PianetaTemaView(
                nome,
                simboloPianeta(nome),
                segno,
                simboloSegno(segno),
                longitudine,
                pianeta.path("isRetrograde").asBoolean(false),
                elementoDi(segno),
                modalitaDi(segno),
                casa,
                numeroRomano(casa));
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
        JsonNode cuspsNode = houses.path("cusps");

        if (!cuspsNode.isArray() || cuspsNode.size() != 12) {
            return 0;
        }

        double longitudineNormalizzata = normalizzaLongitudine(longitudine);
        double[] cuspidi = new double[12];

        for (int i = 0; i < 12; i++) {
            cuspidi[i] = normalizzaLongitudine(
                    cuspsNode.get(i).asDouble());
        }

        for (int i = 0; i < 12; i++) {
            double inizioCasa = cuspidi[i];
            double inizioCasaSuccessiva = cuspidi[(i + 1) % 12];

            boolean dentroCasa;

            if (inizioCasa < inizioCasaSuccessiva) {
                dentroCasa = longitudineNormalizzata >= inizioCasa
                        && longitudineNormalizzata < inizioCasaSuccessiva;
            } else {
                dentroCasa = longitudineNormalizzata >= inizioCasa
                        || longitudineNormalizzata < inizioCasaSuccessiva;
            }

            if (dentroCasa) {
                return i + 1;
            }
        }

        return 0;
    }

    private double normalizzaLongitudine(double longitudine) {
        double normalizzata = longitudine % 360.0;

        if (normalizzata < 0) {
            normalizzata += 360.0;
        }

        return normalizzata;
    }

    public String segnoDaLongitudine(double longitudine) {
        int indice = (int) (normalizzaLongitudine(longitudine) / 30.0);
        return NOMI_SEGNI[indice];
    }

    public String simboloDaSegno(String segno) {
        return simboloSegno(segno);
    }

    private String simboloPianeta(String nome) {
        return switch (nome) {
            case "Sole" -> "\u2609";
            case "Luna" -> "\u263D";
            case "Mercurio" -> "\u263F";
            case "Venere" -> "\u2640";
            case "Marte" -> "\u2642";
            case "Giove" -> "\u2643";
            case "Saturno" -> "\u2644";
            case "Urano" -> "\u2645";
            case "Nettuno" -> "\u2646";
            case "Plutone" -> "\u2647";
            case "Nodo Nord" -> "\u260A";
            case "Lilith" -> "\u26B8";
            case "Chirone" -> "\u26B7";
            default -> "";
        };
    }

    private String simboloSegno(String segno) {
        return switch (segno) {
            case "Ariete" -> "\u2648\uFE0E";
            case "Toro" -> "\u2649\uFE0E";
            case "Gemelli" -> "\u264A\uFE0E";
            case "Cancro" -> "\u264B\uFE0E";
            case "Leone" -> "\u264C\uFE0E";
            case "Vergine" -> "\u264D\uFE0E";
            case "Bilancia" -> "\u264E\uFE0E";
            case "Scorpione" -> "\u264F\uFE0E";
            case "Sagittario" -> "\u2650\uFE0E";
            case "Capricorno" -> "\u2651\uFE0E";
            case "Acquario" -> "\u2652\uFE0E";
            case "Pesci" -> "\u2653\uFE0E";
            default -> "";
        };
    }

    private String numeroRomano(int numero) {
        return switch (numero) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            case 11 -> "XI";
            case 12 -> "XII";
            default -> "-";
        };
    }

    public List<PercentualeTemaView> estraiElementi(
            List<PianetaTemaView> pianeti) {

        return percentuali(
                pianeti,
                PianetaTemaView::elemento,
                Map.of(
                        "Fuoco", "element-fire",
                        "Terra", "element-earth",
                        "Aria", "element-air",
                        "Acqua", "element-water"));
    }

    public List<PercentualeTemaView> estraiModalita(
            List<PianetaTemaView> pianeti) {

        return percentuali(
                pianeti,
                PianetaTemaView::modalita,
                Map.of(
                        "Cardinale", "mode-cardinal",
                        "Fisso", "mode-fixed",
                        "Mobile", "mode-mutable"));
    }

    private List<PercentualeTemaView> percentuali(
            List<PianetaTemaView> pianeti,
            Function<PianetaTemaView, String> classificatore,
            Map<String, String> classiCss) {

        List<PianetaTemaView> principali = pianeti.stream()
                .filter(this::ePianetaPrincipale)
                .toList();

        int totale = principali.size();

        if (totale == 0) {
            return List.of();
        }

        return classiCss.entrySet()
                .stream()
                .map(entry -> {
                    long conteggio = principali.stream()
                            .filter(pianeta -> entry.getKey().equals(
                                    classificatore.apply(pianeta)))
                            .count();

                    int percentuale = (int) Math.round(
                            conteggio * 100.0 / totale);

                    return new PercentualeTemaView(
                            entry.getKey(),
                            percentuale,
                            entry.getValue());
                })
                .toList();
    }

    private boolean ePianetaPrincipale(PianetaTemaView pianeta) {
        return switch (pianeta.nome()) {
            case "Sole", "Luna", "Mercurio", "Venere", "Marte",
                    "Giove", "Saturno", "Urano", "Nettuno", "Plutone" ->
                true;
            default -> false;
        };
    }

}