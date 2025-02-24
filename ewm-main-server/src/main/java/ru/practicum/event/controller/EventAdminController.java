package ru.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.dto.EventAdminUpdateDto;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.service.admin.EventAdminService;
import ru.practicum.event.model.dto.EventUpdateDto;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class EventAdminController {
    private final EventAdminService adminService;

    @GetMapping
    public List<EventFullDto> getAdminEvents(@Valid EventAdminUpdateDto adminUpdateDto) {
        return adminService.getAdminEvents(adminUpdateDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateAdminEvent(@PathVariable @Positive Long eventId,
                                         @RequestBody @Valid EventUpdateDto eventUpdateDto) {
        return adminService.updateAdminEvent(eventId, eventUpdateDto);
    }
}
