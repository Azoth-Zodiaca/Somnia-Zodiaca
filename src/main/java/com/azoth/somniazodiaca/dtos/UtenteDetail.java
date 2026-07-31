package com.azoth.somniazodiaca.dtos;

import com.azoth.somniazodiaca.entities.TemaNatale;
import com.azoth.somniazodiaca.enums.Ruolo;

public record UtenteDetail(
    String username,
    String email,
    Ruolo ruolo,
    SegnoZodiacaleDetail segnoZodiacale,
    SegnoZodiacaleDetail ascendente,
    Integer qi,
    String dataRegistrazione,
    LocalDateTime ultimoAccesso,
    TemaNataleDetail temaNatale

) implements GenericDto {}
