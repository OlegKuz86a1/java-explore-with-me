package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.UnfulfilledConditionException;
import ru.practicum.enums.Status;
import ru.practicum.event.model.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.request.RequestsRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestMapper;
import ru.practicum.request.model.dto.RequestOutDto;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestsServiceImp implements RequestsService {

    private final RequestsRepository requestsRepository;
    private final RequestMapper mapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<RequestOutDto> getUserRequests(Long id) {
        return requestsRepository.findAllByRequesterId(id).stream().map(mapper::toDto).toList();
    }

    @Override
    public RequestOutDto cancelRequest(Long userId, Long requestId) {
        Request request = requestsRepository.findById(requestId).orElseThrow(() -> new NotFoundException(
                String.format("Request with id=%s not found", requestId)));

        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new UnfulfilledConditionException("request for another user");
        }

        request.setStatus(Status.CANCELED);
        Request updateRequest = requestsRepository.save(request);

        return mapper.toDto(updateRequest);
    }

    @Override
    public RequestOutDto addRequest(Long userId, Long eventId) {
        validateRequest(userId, eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException(String.format("Event with id=%s not found", eventId)));
        validateEvent(event, userId);

        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("User with id=%s not found", userId)));

        Request request = Request.builder()
                .requester(requester)
                .event(event)
                .status(event.getParticipantLimit() == 0 ? Status.CONFIRMED :
               (event.getRequestModeration() ? Status.PENDING : Status.CONFIRMED))
                .build();
        return mapper.toDto(requestsRepository.save(request));
    }

    private void validateRequest(Long userId, Long eventId) {
        if (requestsRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new UnfulfilledConditionException(
                    String.format("User with id=%s has already sent a request for event id=%s", userId, eventId));
        }
    }

    private void validateEvent(Event event, Long userId) {
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new UnfulfilledConditionException("The initiator cannot send an event request");
        }

        if (event.getStatus() != Status.PUBLISHED) {
            throw new UnfulfilledConditionException("The event has not been published, applications are not accepted");
        }

        if (event.getParticipantLimit() > 0) {
            int confirmationRequests = requestsRepository.countByEventIdAndStatus(event.getId(), Status.CONFIRMED);
            if (confirmationRequests >= event.getParticipantLimit())
                throw new UnfulfilledConditionException(
                        String.format("Number of requests exceeded for the event id=%s ", event.getId()));
        }
    }
}
