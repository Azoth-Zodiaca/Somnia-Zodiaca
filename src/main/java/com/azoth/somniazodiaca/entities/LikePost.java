package com.azoth.somniazodiaca.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "like_post", uniqueConstraints = @UniqueConstraint(name = "uk_like_post_utente", columnNames = {
        "post_id", "utente_id" }))
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LikePost extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;
}