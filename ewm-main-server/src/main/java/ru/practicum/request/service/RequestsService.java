package ru.practicum.request.service;

import ru.practicum.request.model.dto.RequestOutDto;

import java.util.List;

public interface RequestsService {
    List<RequestOutDto> getUserRequests(Long id);

    RequestOutDto addRequest(Long userid, Long eventId);

    RequestOutDto cancelRequest(Long userId, Long requestId);

}
