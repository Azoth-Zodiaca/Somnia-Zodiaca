package com.azoth.somniazodiaca.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Service
public class GeminiService {

    private final Client client;

    public GeminiService(@Value("${gemini.api-key}") String apiKey) {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public String askGemini(String prompt) {
        GenerateContentResponse response = client.models.generateContent(
                "gemini-3.6-flash",
                // "gemini-3.7-flash",
                prompt,
                null);

        return response.text();
    }

    public String interpretNatalChart(String chartResponse, String astroWayInterpretation) {
        String prompt = """
                Sei l'editor astrologico di Somnia Zodiaca.
                Scrivi esclusivamente JSON valido, senza markdown e senza blocchi ```.
                Rispondi esclusivamente in italiano naturale, chiaro e sintetico.
                Usa solo i dati presenti nel materiale fornito: non inventare posizioni o aspetti.
                Il testo deve essere contestuale a una profilazione caratteriale e relazionale,
                ma deve parlare di tendenze simboliche e non di certezze o previsioni.
                Produci esattamente questo schema JSON:
                {
                  "sintesi": "breve profilo generale",
                  "triade": {"sole": "", "luna": "", "ascendente": ""},
                  "pianeti": [{"nome": "", "posizione": "", "testo": "", "relazioni": ""}],
                  "case": [{"numero": 1, "titolo": "", "significato": "", "testo": ""}],
                  "compatibilita": "elementi e dinamiche relazionali più affini",
                  "puntiForza": ["", ""],
                  "areeCrescita": ["", ""]
                }
                Mantieni la risposta fruibile: sintesi 80-120 parole, triade 40-70 parole per voce,
                pianeti massimo 10 elementi e 35-55 parole per testo, case massimo 12 elementi e
                25-45 parole per testo, compatibilita 70-100 parole.

                DATI CALCOLATI DA ASTROWAY:
                """ + chartResponse + """

                LETTURA ASTROWAY DA RIELABORARE:
                """ + astroWayInterpretation;

        return askGemini(prompt);
    }
}