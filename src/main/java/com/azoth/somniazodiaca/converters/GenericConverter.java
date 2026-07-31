package com.azoth.somniazodiaca.converters;

public interface GenericConverter<E, D> {

    E fromDToE(D dto);

    D fromEToD(E entity);
}
