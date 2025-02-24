package ru.practicum.event.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import ru.practicum.event.model.dto.EventAdminUpdateDto;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventUpdateDto;
import ru.practicum.request.RequestsRepository;
import ru.practicum.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventAdminServiceImpl implements EventAdminService {
    private final EventRepository eventRepo;
    private final RequestsRepository requestsRepo;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepo;

    @Override
    public List<EventFullDto> getAdminEvents(EventAdminUpdateDto adminUpdateDto) {
        Pageable pageable = PageRequest.of(adminUpdateDto.getFrom(), adminUpdateDto.getSize());
        Specification<Event> specification = buildEventSpecification(adminUpdateDto);
        List<Event> events = eventRepo.findAll(specification, pageable).toList();

        List<Request> requests = requestsRepo.findAllByEventIdInAndStatus(events
                .stream().map(Event::getId).collect(Collectors.toList()), Status.CONFIRMED);

        Map<Long, List<Request>> countConfirmedReq = requests.stream()
                .collect(Collectors.groupingBy(r -> r.getEvent().getId()));

        return events.stream()
                .map(eventMapper::mapEventToFullDto)
                .peek(event -> {
                    List<Request> requestList = countConfirmedReq.getOrDefault(event.getId(), List.of());
                    event.setConfirmedRequests(requestList.size());
                })
                .toList();
    }

    @Override
    public EventFullDto updateAdminEvent(Long eventId, EventUpdateDto updateDto) {
        Event eventOld = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("Event with id=%s not found", eventId)));

        if (eventOld.getStatus().equals(Status.PUBLISHED) || eventOld.getStatus().equals(Status.CANCELED)) {
            throw new UnfulfilledConditionException("You can only change an unconfirmed event");
        }

        Event eventUpdate = checkingAndUpdatingEvent(updateDto, eventOld);
        EventFullDto eventFullDto = eventMapper.mapEventToFullDto(eventRepo.save(eventUpdate));
        log.info("Event in admin service has been successfully updated, with id={} ", eventFullDto.getId());
        return eventFullDto;
    }

    private Specification<Event> buildEventSpecification(EventAdminUpdateDto params) {
        Specification<Event> spec = Specification.where(null);
        List<Long> users = params.getUsers();
        List<String> states = params.getStates();
        List<Long> categories = params.getCategories();
        LocalDateTime rangeEnd = params.getRangeEnd();
        LocalDateTime rangeStart = params.getRangeStart();

        if (users != null && !users.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("initiator").get("id").in(users));
        }

        if (states != null && !states.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("status").in(states));
        }

        if (categories != null && !categories.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }

        if (rangeEnd != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }

        if (rangeStart != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }
        return spec;
    }

    private Event checkingAndUpdatingEvent(EventUpdateDto update, Event eventOld) {
        if (update.getAnnotation() != null) eventOld.setAnnotation(update.getAnnotation());
        if (update.getTitle() != null) eventOld.setTitle(update.getTitle());
        if (update.getDescription() != null) eventOld.setDescription(update.getDescription());
        if (update.getEventDate() != null) eventOld.setEventDate(update.getEventDate());
        if (update.getPaid() != null) eventOld.setPaid(update.getPaid());
        if (update.getParticipantLimit() != null) eventOld.setParticipantLimit(update.getParticipantLimit());
        if (update.getRequestModeration() !=null) eventOld.setRequestModeration(update.getRequestModeration());
        if (update.getCategory() != null && !update.getCategory().equals(eventOld.getCategory().getId())) {
            eventOld.setCategory(categoryValidator(update.getCategory()));
        }

        if (update.getLocation() != null) {
            eventOld.setLat(update.getLocation().getLat());
            eventOld.setLon(update.getLocation().getLon());
        }

        StateAction state = update.getState();
        if (state != null) {
            switch (state) {
                case PUBLISH_EVENT:
                    eventOld.setStatus(Status.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    eventOld.setStatus(Status.CANCELED);
                    break;
            }
        }
        return eventOld;
    }

    private Category categoryValidator(Long catId) {
        return categoryRepo.findById(catId).orElseThrow(() -> new NotFoundException(
                String.format("Category with id=%s not found", catId)));
    }
}
