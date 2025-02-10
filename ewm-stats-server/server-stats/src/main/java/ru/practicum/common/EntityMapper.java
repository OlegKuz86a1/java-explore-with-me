package ru.practicum.common;

public interface EntityMapper<D, E> {
    E toEntity(D dto);
}
