package com.azoth.somniazodiaca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
