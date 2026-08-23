package com.azoth.somniazodiaca.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "utenti_follow", uniqueConstraints = @UniqueConstraint(name = "uk_utenti_follow", columnNames = {
        "follower_id", "seguito_id" }))
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UtenteFollow extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "follower_id", nullable = false)
    private Utente follower;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seguito_id", nullable = false)
    private Utente seguito;
}