package com.azoth.somniazodiaca.converters;

import java.util.List;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.CommentoDto;
import com.azoth.somniazodiaca.dtos.PostDto;
import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.entities.Post;
import com.azoth.somniazodiaca.entities.SegnoZodiacale;
import com.azoth.somniazodiaca.entities.Utente;
import com.azoth.somniazodiaca.enums.Ruolo;

@Service
public class PostConverter implements GenericConverter<Post, PostDto> {

    public Post fromDToE(PostDto d) {
        Post e = Post.builder()
                .id(d.getId())
                .testoVisibile(d.getTestoVisibile())
                .dataPubblicazione(d.getDataPubblicazione())
                .numeroLike(d.getNumeroLike())
                .build();

        if (d.getUtenteId() != null) {
            Utente utente = Utente.builder().id(d.getUtenteId()).build();
            e.setUtente(utente);
        }

        if (d.getInterpretazioneId() != null) {
            Interpretazione interpretazione = Interpretazione.builder().id(d.getInterpretazioneId()).build();
            e.setInterpretazione(interpretazione);
        }

        return e;
    }

    public PostDto fromEToD(Post e) {
        var utente = e.getUtente();
        var interpretazione = e.getInterpretazione();

        return PostDto.builder()
                .id(e.getId())
                .utenteId(utente != null ? utente.getId() : null)
                .interpretazioneId(interpretazione != null
                        ? interpretazione.getId()
                        : null)
                .testoVisibile(e.getTestoVisibile())
                .dataPubblicazione(e.getDataPubblicazione())
                .numeroLike(e.getNumeroLike())
                .numeroCommenti(e.getCommenti() != null
                        ? e.getCommenti().size()
                        : 0)
                .commenti(convertiCommenti(e.getCommenti()))
                .username(utente != null
                        ? utente.getUsername()
                        : "Utente")
                .premium(utente != null
                        && utente.getRuolo() == Ruolo.PREMIUM)
                .avatarPath(utente != null
                        ? utente.getAvatarPath()
                        : null)
                .segnoZodiacale(nomeSegno(utente != null
                        ? utente.getSegnoZodiacale()
                        : null))
                .segnoZodiacaleAbbreviato(abbreviaSegno(utente != null
                        ? utente.getSegnoZodiacale()
                        : null))
                .segnoZodiacaleSimbolo(simboloSegno(utente != null
                        ? utente.getSegnoZodiacale()
                        : null))
                .ascendente(nomeSegno(utente != null
                        ? utente.getAscendente()
                        : null))
                .ascendenteSimbolo(simboloSegno(utente != null
                        ? utente.getAscendente()
                        : null))
                .testoSogno(interpretazione != null
                        && interpretazione.getSogno() != null
                                ? interpretazione.getSogno().getTesto()
                                : null)
                .testoInterpretazione(interpretazione != null
                        ? interpretazione.getTesto()
                        : null)
                .likedByCurrentUser(false)
                .build();
    }

    private List<CommentoDto> convertiCommenti(
            List<com.azoth.somniazodiaca.entities.Commento> commenti) {

        if (commenti == null) {
            return List.of();
        }

        return commenti.stream()
                .<CommentoDto>map(commento -> CommentoDto.builder()
                        .id(commento.getId())
                        .username(commento.getUtente() != null
                                ? commento.getUtente().getUsername()
                                : "Utente")
                        .testo(commento.getTesto())
                        .dataCreazione(commento.getCreatedAt())
                        .build())
                .toList();
    }

    private String nomeSegno(SegnoZodiacale segno) {
        return segno == null || segno.getSegnoZodiacale() == null
                ? null
                : segno.getSegnoZodiacale().name();
    }

        private String abbreviaSegno(SegnoZodiacale segno) {
                if (segno == null || segno.getSegnoZodiacale() == null) {
                        return null;
                }

                return switch (segno.getSegnoZodiacale().name()) {
                        case "ARIETE" -> "Ari";
                        case "TORO" -> "Tau";
                        case "GEMELLI" -> "Gem";
                        case "CANCRO" -> "Can";
                        case "LEONE" -> "Leo";
                        case "VERGINE" -> "Vir";
                        case "BILANCIA" -> "Lib";
                        case "SCORPIONE" -> "Sco";
                        case "SAGITTARIO" -> "Sag";
                        case "CAPRICORNO" -> "Cap";
                        case "ACQUARIO" -> "Aqu";
                        case "PESCI" -> "Pis";
                        default -> nomeSegno(segno);
                };
        }

        private String simboloSegno(SegnoZodiacale segno) {
                if (segno == null || segno.getSegnoZodiacale() == null) {
                        return null;
                }

                return switch (segno.getSegnoZodiacale().name()) {
                        case "ARIETE" -> "\u2648\uFE0E";
                        case "TORO" -> "\u2649\uFE0E";
                        case "GEMELLI" -> "\u264A\uFE0E";
                        case "CANCRO" -> "\u264B\uFE0E";
                        case "LEONE" -> "\u264C\uFE0E";
                        case "VERGINE" -> "\u264D\uFE0E";
                        case "BILANCIA" -> "\u264E\uFE0E";
                        case "SCORPIONE" -> "\u264F\uFE0E";
                        case "SAGITTARIO" -> "\u2650\uFE0E";
                        case "CAPRICORNO" -> "\u2651\uFE0E";
                        case "ACQUARIO" -> "\u2652\uFE0E";
                        case "PESCI" -> "\u2653\uFE0E";
                        default -> "";
                };
        }
}
