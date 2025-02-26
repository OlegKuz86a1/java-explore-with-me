package ru.practicum.event.service.priv;

import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventInDto;
import ru.practicum.event.model.dto.EventOutDto;
import ru.practicum.event.model.dto.EventUpdateDto;
import ru.practicum.request.model.dto.RequestOutDto;
import ru.practicum.request.model.dto.RequestStatusEventUpdateRequest;
import ru.practicum.request.model.dto.RequestStatusEventUpdateResult;

import java.util.List;

public interface EventPrivateService {

    List<EventOutDto> getAllPrivateEvent(Long userId, int from, int size);

    EventFullDto addPrivateEvent(Long userId, EventInDto eventInDto);

    EventFullDto getPrivateEvent(Long userId, Long eventId);

    EventFullDto updatePrivateEvent(Long userId, Long eventId, EventUpdateDto eventUpdateDto);

    List<RequestOutDto> getAllPrivateEventRequests(Long userId, Long eventId);

    RequestStatusEventUpdateResult updateRequestPrivateEvent(Long userId, Long eventId,
                                                             RequestStatusEventUpdateRequest request);
}

