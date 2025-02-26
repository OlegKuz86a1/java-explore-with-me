package ru.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventInDto;
import ru.practicum.event.model.dto.EventOutDto;
import ru.practicum.event.model.dto.EventUpdateDto;
import ru.practicum.event.service.priv.EventPrivateService;
import ru.practicum.request.model.dto.RequestOutDto;
import ru.practicum.request.model.dto.RequestStatusEventUpdateRequest;
import ru.practicum.request.model.dto.RequestStatusEventUpdateResult;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {
    private final EventPrivateService privateService;

    @GetMapping
    public List<EventOutDto> getAllPrivateEvent(@PathVariable @Positive Long userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10")
                                                @Min(value = 1, message = "The min allowed value for the size is 1")
                                                int size) {
        log.info("request to receive user events with id= {}", userId);
        return privateService.getAllPrivateEvent(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addPrivateEvent(@PathVariable @Positive Long userId,
                                        @RequestBody @Valid EventInDto eventInDto) {
        return privateService.addPrivateEvent(userId, eventInDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getPrivateEvent(@PathVariable @Positive Long userId,
                                        @PathVariable @Positive Long eventId) {
        return privateService.getPrivateEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updatePrivateEvent(@PathVariable @Positive Long userId,
                                           @PathVariable @Positive Long eventId,
                                           @RequestBody @Valid EventUpdateDto eventUpdateDto) {
        return privateService.updatePrivateEvent(userId, eventId, eventUpdateDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestOutDto> getAllPrivateEventRequests(@PathVariable @Positive Long userId,
                                                          @PathVariable @Positive Long eventId) {
        return privateService.getAllPrivateEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestStatusEventUpdateResult updateRequestPrivateEvent(@PathVariable @Positive Long userId,
                                                                    @PathVariable @Positive Long eventId,
                                                                    @RequestBody RequestStatusEventUpdateRequest request) {
        return privateService.updateRequestPrivateEvent(userId, eventId, request);
    }
}
