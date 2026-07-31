package com.azoth.somniazodiaca.converters;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.dtos.PostDto;
import com.azoth.somniazodiaca.entities.Interpretazione;
import com.azoth.somniazodiaca.entities.Post;
import com.azoth.somniazodiaca.entities.Utente;

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
        return PostDto.builder()
                .id(e.getId())
                .utenteId(e.getUtente() != null ? e.getUtente().getId() : null)
                .interpretazioneId(e.getInterpretazione() != null ? e.getInterpretazione().getId() : null)
                .testoVisibile(e.getTestoVisibile())
                .dataPubblicazione(e.getDataPubblicazione())
                .numeroLike(e.getNumeroLike())
                .build();
    }
}
