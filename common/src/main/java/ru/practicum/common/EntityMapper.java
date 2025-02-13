package ru.practicum.common;

import java.util.List;

public interface EntityMapper<D, E> {

    E toEntity(D dto);

    List<E> toListEntity(List<D> listDto);

//  D toDto(E entity);
//
//  List<D> toListDto(List<E> listEntity);
}
