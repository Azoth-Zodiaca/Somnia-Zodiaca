package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

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
                .username(utente != null
                        ? utente.getUsername()
                        : "Utente")
                .premium(utente != null
                        && utente.getRuolo() == Ruolo.PREMIUM)
                .avatarPath(utente != null
                        ? utente.getAvatarPath()
                        : null)
                .profiloColore(utente != null
                        ? utente.getProfiloColore()
                        : null)
                .segnoZodiacale(nomeSegno(
                        utente != null
                                ? utente.getSegnoZodiacale()
                                : null))
                .ascendente(nomeSegno(
                        utente != null
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

    private String nomeSegno(SegnoZodiacale segno) {
        return segno == null || segno.getSegnoZodiacale() == null
                ? null
                : segno.getSegnoZodiacale().name();
    }
}
