package com.azoth.somniazodiaca.entities;

import java.time.LocalDateTime;

import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

//in questo modo indico che questa classe non è un'entità lato db per cui 
//non corrisponde ad una tabella autonoma ma i suoi campi
//verranno mappati e veraano erediatti nelle tabelle delle classi figlie
//eredità di mapping
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * @Id identifica la chiave primaria JPA.
     * @GeneratedValue con IDENTITY delega a MySQL la generazione tramite AUTO_INCREMENT.
     * La scelta fatta qui deve coerente con lo schema Flyway perchè
     * non sarà hibernate a creare la struttura del db ma lo script sql,
     * hibernate è invece in modalità validate quindi validazione ma non modifica della struttura del db.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",updatable = false)
    private Long id;

    //se per caso voglio inserire proprietà solo lato Java 
    //e non lato db, quindi questa non la voglio mappare ad una colonna del db
    //uso @Transient
    // @Transient 
    // private String alias;


    /**
     * @CreatedDate viene gestita da Spring Data JPA Auditing al primo salvataggio.
     * per cui se l'auditing è abilitato quando trova questa annotazione
     * valorizza il campo associato ad uno specifico evento, questo caso è una INSERT
     * cioè quando viene persistita la entity hibernate salva qui dentro data e ora della creazione
     * <br>
     * updatable=false impedisce che Hibernate provi a cambiare la data di creazione
     * negli aggiornamenti successivi.
     */
    @CreatedDate
    @Column(name="created_at", nullable = false,updatable = false)
    private LocalDateTime createdAt;

    /**
     * @LastModifiedDate viene aggiornata automaticamente a ogni modifica dell'entity.
     * sempre JPA auditing si attiva in update e salva qui dentro ora e data correnti
     * Per cui ogni volta che l'entità viene aggiornata (cioè in un'UPDATE di anche solo una proprietà), 
     * hibernate aggiorna qui dentro data e ora dell'aggiornamento
     */
    @LastModifiedDate
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;



    //l'identità è basata sull'id quindi sulla pk solo dopo che l'entità è 
    //stata salvata lato db, cioè PERSISTITA
    //in questo modo se due entità che sono già persistite hanno lo stesso id allora sono la stessa entità
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || Hibernate.getClass(this) !=  Hibernate.getClass(obj))
            return false;
        BaseEntity other = (BaseEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }

    

}
