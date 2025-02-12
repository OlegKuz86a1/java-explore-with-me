package ru.practicum.common;

import java.util.List;

public interface EntityMapper<D, E> {

    E toEntity(D dto);

//    D toDto(E entity);
//
//    List<D> toListDto(List<E> listEntity);
//
//    List<E> toListEntity(List<D> listDto);
}
