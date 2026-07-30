package com.azoth.somniazodiaca.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azoth.somniazodiaca.converters.GenericConverter;
import com.azoth.somniazodiaca.dtos.GenericDto;
import com.azoth.somniazodiaca.entities.BaseEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class GenericService <
                                        ID,
                                        E extends BaseEntity,
                                        D extends GenericDto,
                                        C extends GenericConverter <E,D>,
                                        R extends JpaRepository <E,ID>
                                    >{
    
    private final R repository;
    private final C converter;
     
    // // fe  - ->   CONTROLLER   -(D)->   SERVICE    --(E)->  REPO ---> db
    // public abstract E construct(Map<String,String> params); //TODO: per adesso non usiamo il context

    public List<D> getAll(){
        List<E> lista = repository.findAll();
        List<D> listaDTO = lista.stream().map(
            e ->  converter.fromEToD(e)
        ).toList();
        return listaDTO;
    }

    public D getById(ID id){
        E e = repository.findById(id).get();
        D d = converter.fromEToD(e);
        return d;
    }

     // save() effettua una insert o una update, per decidere controlla se l'id è già presente nel DB
    public boolean save(D dto){
        try {
            E e = converter.fromDToE(dto);
            repository.save(e);
            return true;
        } catch (Exception excep) {
            excep.printStackTrace();
            return false;
        }
    }

    /*
        1) dal CONTROLLER mi arriva una mappa 
            voglio trasformare la mappa in entità da passare al repository
        2) per questa trasformazione uso fromMap() 
            -> ho già a disposizione una classe che fa questo lavoro per me  -> EntityContext (factory di Spring)
            -> invoco EntityContext tramite il metodo construct() che ho implementato
        3) a questo punto ho una entity e posso passarla alla repository
     */

    public void delete(ID id){
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

}