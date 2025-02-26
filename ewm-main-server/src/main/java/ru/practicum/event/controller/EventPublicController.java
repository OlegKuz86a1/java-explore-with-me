package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventOutDto;
import ru.practicum.event.model.dto.EventPublicRequestDto;
import ru.practicum.event.service.pub.EventPublicService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventPublicController {
    private final EventPublicService publicService;

    @GetMapping
    public List<EventOutDto> getPublicEvents(@Valid EventPublicRequestDto eventPublicRequestDto,
                                             HttpServletRequest servletRequest) {
        return publicService.getPublicEvents(eventPublicRequestDto, servletRequest);
    }

    @GetMapping("/{id}")
    public EventFullDto getPublicFullEvent(@PathVariable @Positive Long id, HttpServletRequest servletRequest) {
        return publicService.getPublicFullEvent(id, servletRequest);
    }
}
