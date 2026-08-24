package com.azoth.somniazodiaca.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class PostDto implements GenericDto {

    private Long id;

    private Long utenteId;
    private Long interpretazioneId;

    private String testoVisibile;
    private LocalDateTime dataPubblicazione;
    private Integer numeroLike;

    private Integer numeroCommenti;
    private List<CommentoDto> commenti;

    private String username;
    private boolean premium;
    private String avatarPath;
    private String profiloColore;

    private String segnoZodiacale;
    private String ascendente;

    private String testoSogno;
    private String testoInterpretazione;

    private boolean likedByCurrentUser;

    private boolean seguitoDaCurrentUser;
}
