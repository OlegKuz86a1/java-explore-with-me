package ru.practicum.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.model.dto.RequestOutDto;
import ru.practicum.request.service.RequestsService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Validated
@Slf4j
@RequiredArgsConstructor
public class RequestsController {
    private final RequestsService requestsService;

    @GetMapping
    public List<RequestOutDto> getUserRequests(@PathVariable @Positive Long userId) {
        return requestsService.getUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestOutDto addRequest(@PathVariable @PositiveOrZero Long userId,
                                    @RequestParam @Positive Long eventId) {
        RequestOutDto requestOutDto = requestsService.addRequest(userId, eventId);
        log.info("New request has been successfully added, with id={} ", requestOutDto.getId());
        return requestOutDto;
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestOutDto cancelRequest(@PathVariable @Positive Long userId,
                                       @PathVariable @Positive Long requestId) {
        RequestOutDto requestOutDto = requestsService.cancelRequest(userId, requestId);
        log.info("status of request id={} changed to CANCELED", requestId);
        return requestOutDto;
    }
}
