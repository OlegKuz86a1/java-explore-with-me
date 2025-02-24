package ru.practicum.event.service.admin;

import ru.practicum.event.model.dto.EventAdminUpdateDto;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventUpdateDto;

import java.util.List;

public interface EventAdminService {

    List<EventFullDto> getAdminEvents(EventAdminUpdateDto adminUpdateDto);

    EventFullDto updateAdminEvent(Long eventId, EventUpdateDto updateDto);
}
