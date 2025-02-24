package ru.practicum.event.service.priv;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.UnfulfilledConditionException;
import ru.practicum.enums.StateAction;
import ru.practicum.enums.Status;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.model.dto.*;
import ru.practicum.request.RequestsRepository;
import ru.practicum.request.model.RequestMapper;
import ru.practicum.request.model.dto.RequestOutDto;
import ru.practicum.request.model.dto.RequestStatusEventUpdateRequest;
import ru.practicum.request.model.dto.RequestStatusEventUpdateResult;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.enums.Status.CONFIRMED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPrivateServiceImpl implements EventPrivateService{

    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final RequestsRepository requestsRepo;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepo;
    private final RequestMapper requestMapper;
    private final EntityManager entityManager;

    @Override
    public List<EventOutDto> getAllPrivateEvent(Long userId, int from, int size) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s not found", userId));
        }

        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC,"id"));

        return eventRepo.findAll(pageable).getContent().stream()
                .map(eventMapper::toDto)
                .toList();
    }

    @Override
    public EventFullDto addPrivateEvent(Long userId, EventInDto eventInDto) {
        User initiator = userValidator(userId);
        Category category = categoryValidator(eventInDto.getCategory());

        Event event = eventMapper.mapInDtoToEvent(eventInDto);
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setParticipantLimit(eventInDto.getParticipantLimit());

        Event saved = eventRepo.save(event);
        EventFullDto eventFullDto = eventMapper.mapEventToFullDto(eventRepo.save(event));

        initializeDefaultValues(eventFullDto);
        log.info("New Event has been successfully added, with id={} ", saved.getId());
        return eventFullDto;
    }

    @Override
    public EventFullDto getPrivateEvent(Long userId, Long eventId) {
        userValidator(userId);
        Event event = eventValidatorByUserIdAndEventId(userId, eventId);
        log.info("event received by user id {} and event id {}",userId, eventId);
        return eventMapper.mapEventToFullDto(event);
    }

    @Override
    public EventFullDto updatePrivateEvent(Long userId, Long eventId, EventUpdateDto eventUpdateDto) {
        userValidator(userId);
        Event eventOld = eventValidatorByUserIdAndEventId(userId, eventId);

        Event withChanges = checkingAndUpdatingEvent(eventUpdateDto, eventOld);
        EventFullDto eventFullDto = eventMapper.mapEventToFullDto(eventRepo.save(withChanges));
        log.info("Event in private service has been successfully updated, with id={} ", eventFullDto.getId());
        return eventFullDto;
    }

    @Override
    public List<RequestOutDto> getAllPrivateEventRequests(Long userId, Long eventId) {
        eventValidatorByUserIdAndEventId(userId, eventId);
        return requestsRepo.findAllByEventId(eventId).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    public RequestStatusEventUpdateResult updateRequestPrivateEvent(Long userId, Long eventId,
                                                                    RequestStatusEventUpdateRequest request) {
        userValidator(userId);
        Event event = eventValidatorByUserIdAndEventId(userId, eventId);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new UnfulfilledConditionException("his event does not require confirmation of requests.");
        }

        Status status = request.getStatus();

        if (status == CONFIRMED) {
            int countRequest = requestsRepo.countByEventIdAndStatus(event.getId(), CONFIRMED);
            if (event.getParticipantLimit() < countRequest + request.getRequestIds().size()) {
                throw new UnfulfilledConditionException("Limit user participation");
            }
        }
        if (status == Status.REJECTED) {
            if (requestsRepo.findAllById(request.getRequestIds()).stream()
                    .anyMatch(r -> r.getStatus() == CONFIRMED)) {
                throw new UnfulfilledConditionException("Trying to reject already confirmed request");
            }
        }

        requestsRepo.updateStatus(status, request.getRequestIds());

        entityManager.clear();

        Map<Status, List<RequestOutDto>> requestsByStatus = requestsRepo.findAllByEventId(eventId).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.groupingBy(RequestOutDto::getStatus, Collectors.toList()));

        return RequestStatusEventUpdateResult.builder()
                .confirmedRequests(requestsByStatus.getOrDefault(CONFIRMED, new ArrayList<>()))
                .rejectedRequests(requestsByStatus.getOrDefault(Status.REJECTED, new ArrayList<>()))
                .build();
    }

    private User userValidator(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("User with id=%s not found", userId)));
    }

    private Category categoryValidator(Long catId) {
        return categoryRepo.findById(catId).orElseThrow(() -> new NotFoundException(
                String.format("Category with id=%s not found", catId)));
    }

    private void initializeDefaultValues(EventFullDto eventFullDto) {
        eventFullDto.setViews(0L);
        eventFullDto.setConfirmedRequests(0);
    }

    private Event eventValidatorByUserIdAndEventId(Long userId, Long eventId) {
        return eventRepo.findByInitiatorIdAndId(userId, eventId).orElseThrow(() -> new NotFoundException(
                String.format("Event with id=%s and initiator id=%s not found", eventId, userId)));
    }

    private Event checkingAndUpdatingEvent(EventUpdateDto update, Event eventOld) {
        Status correctStatus = eventOld.getStatus();

        if (correctStatus.equals(Status.PUBLISHED)) {
            throw new UnfulfilledConditionException("An event with the PUBLISHED status cannot be changed");
        }

        if (update.getAnnotation() != null) eventOld.setAnnotation(update.getAnnotation());

        if (update.getCategory() != null && !update.getCategory().equals(eventOld.getCategory().getId())) {
            eventOld.setCategory(categoryValidator(update.getCategory()));
        }

        if (update.getLocation() != null) {
            eventOld.setLat(update.getLocation().getLat());
            eventOld.setLon(update.getLocation().getLon());
        }

        if (update.getTitle() != null) eventOld.setTitle(update.getTitle());
        if (update.getDescription() != null) eventOld.setDescription(update.getDescription());
        if (update.getEventDate() != null) eventOld.setEventDate(update.getEventDate());
        if (update.getPaid() != null) eventOld.setPaid(update.getPaid());
        if (update.getParticipantLimit() != null) eventOld.setParticipantLimit(update.getParticipantLimit());
        if (update.getRequestModeration() !=null) eventOld.setRequestModeration(update.getRequestModeration());

        StateAction state = update.getState();
        if (state != null) {
            switch (state) {
                case SEND_TO_REVIEW:
                    eventOld.setStatus(Status.PENDING);
                    break;
                case CANCEL_REVIEW:
                    eventOld.setStatus(Status.CANCELED);
                    break;
            }
        }
        return eventOld;
    }
}
