package com.azoth.somniazodiaca.services;

import com.azoth.somniazodiaca.converters.PostConverter;
import com.azoth.somniazodiaca.dtos.PostDto;
import com.azoth.somniazodiaca.entities.Post;

public class PostService extends GenericService<Long, Post, PostDto, PostConverter, PostRepository> {

    public PostService(PostRepository repository, PostConverter converter) {
        super(repository, converter);
    }

}
