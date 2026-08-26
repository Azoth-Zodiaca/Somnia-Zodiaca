package com.azoth.somniazodiaca.dtos.records;

public record PianetaTemaView(
                String nome,
                String simbolo,
                String segno,
                String simboloSegno,
                double longitudine,
                boolean retrogrado,
                String elemento,
                String modalita,
                int casa,
                String casaRomana) {
}