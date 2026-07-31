package com.azoth.somniazodiaca.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.azoth.somniazodiaca.converters.PostConverter;
import com.azoth.somniazodiaca.dtos.PostDto;
import com.azoth.somniazodiaca.entities.Post;
import com.azoth.somniazodiaca.repositories.PostRepository;

@Service
public class PostService extends GenericService<Long, Post, PostDto, PostConverter, PostRepository> {

    public PostService(PostRepository repository, PostConverter converter) {
        super(repository, converter);
    }

    public List<PostDto> findByUtenteId(Long utenteId) {
        return getRepository().findByUtenteIdOrderByDataPubblicazioneDesc(utenteId).stream().map(getConverter()::fromEToD).toList();
    }
}
