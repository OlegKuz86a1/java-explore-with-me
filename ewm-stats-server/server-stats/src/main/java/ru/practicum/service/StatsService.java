package ru.practicum.service;

import ru.practicum.RequestHitsOutDto;
import ru.practicum.RequestInDto;

import java.util.List;

public interface StatsService {
    void saveRequest(RequestInDto requestInDto);

    List<RequestHitsOutDto> getStats(String start, String end, List<String> uris, boolean unique);
}
