package ru.practicum.event.service.pub;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventOutDto;
import ru.practicum.event.model.dto.EventPublicRequestDto;

import java.util.List;

public interface EventPublicService {

    List<EventOutDto> getPublicEvents(EventPublicRequestDto eventPublicRequestDto, HttpServletRequest request);

    EventFullDto getPublicFullEvent(Long eventId, HttpServletRequest request);
}
