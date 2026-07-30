package com.azoth.somniazodiaca.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//questa classe sarà un bean gestito da spring ed è una sorgente di configurazione
@Configuration

//attiva l'infrastruttura di auditing di spring JPA
//intercetta degli eventi, se sono annotati correttamente, del ciclo di vita dei Bean
//- può valorizzare alcuni campi prima di salvare un'entità/prima/dopo un aggiornamento
//- JPA si mtte in ascolto di un evento
//pre insert/pre update
//se l'evento avviene valorizza determinati campi annotati in un certo modo 
@EnableJpaAuditing
public class JpaConfig {


}
