package com.azoth.somniazodiaca.converters;

import com.azoth.somniazodiaca.dtos.GenericDto;
import com.azoth.somniazodiaca.entities.BaseEntity;

public interface GenericConverter <E extends BaseEntity, D extends GenericDto> {
    public E fromDToE(D d);
    public D fromEToD(E e);
}
