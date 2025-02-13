package ru.practicum.repository;

import ru.practicum.model.RequestHits;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsCustomRepository {
    List<RequestHits> getRequestHits(LocalDateTime start, LocalDateTime end, List<String> uri, boolean unique);
}
