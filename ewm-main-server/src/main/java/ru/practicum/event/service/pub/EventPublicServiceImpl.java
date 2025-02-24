package ru.practicum.event.service.pub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.RequestHitsOutDto;
import ru.practicum.RequestInDto;
import ru.practicum.common.exception.IncorrectParametersException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.enums.Status;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventOutDto;
import ru.practicum.event.model.dto.EventPublicRequestDto;
import ru.practicum.stats.ClientStats;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublicServiceImpl implements EventPublicService{

    private final EventRepository eventRepo;
    private final EventMapper eventMapper;
    private final ClientStats clientStats;
    private final ObjectMapper objectMapper;

    @Value("${server.application.name:ewm-service}")
    private String application;

    @Override
    public List<EventOutDto> getPublicEvents(EventPublicRequestDto requestParam, HttpServletRequest request) {

        if (requestParam.getRangeEnd() != null &&
                requestParam.getRangeStart() != null &&
                requestParam.getRangeEnd().isBefore(requestParam.getRangeStart())) {
            throw new IncorrectParametersException("The end date cannot be earlier than the start date");
        }

        addClientStats(request);
        Pageable pageable = PageRequest.of(requestParam.getFrom(), requestParam.getSize());
        Specification<Event> specification = buildEventSpecification(requestParam);
        List<Event> events = eventRepo.findAll(specification, pageable).getContent();
        Map<Long, Long> views = getViews(events);

        return events.stream()
                .map(eventMapper::toDto)
                .peek(dto -> dto.setViews(views.getOrDefault(dto.getId(), 0L)))
                .toList();
    }


    @Override
    public EventFullDto getPublicFullEvent(Long eventId, HttpServletRequest request) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException(
                String.format("Event with id=%s not found", eventId)));

        if (!event.getStatus().equals(Status.PUBLISHED)) {
            throw new NotFoundException(String.format("Event with id=%s not PUBLISHED", eventId));
        }

        addClientStats(request);
        EventFullDto eventFullDto = eventMapper.mapEventToFullDto(event);
        Map<Long, Long> views = getViews(List.of(event));
        eventFullDto.setViews(views.getOrDefault(event.getId(), 0L));

        return eventFullDto;
    }

    private Specification<Event> buildEventSpecification(EventPublicRequestDto requestParam) {
        Specification<Event> spec = Specification.where(null);
        List<Long> categories = requestParam.getCategories();
        Boolean paid = requestParam.getPaid();
        LocalDateTime rangeStart = requestParam.getRangeStart();
        LocalDateTime rangeEnd = requestParam.getRangeEnd();
        Boolean onlyAvailable = requestParam.getOnlyAvailable();


        if (requestParam.getText() != null) {
            String textLow = requestParam.getText().toLowerCase();
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + textLow + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + textLow + "%")
            ));
        }

        if (categories != null ) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories)));
        }

        if(paid != null) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    paid ? criteriaBuilder.isTrue(root.get("paid"))
                            : criteriaBuilder.isFalse(root.get("paid"))));
        }

        LocalDateTime start = Objects.requireNonNullElse(rangeStart, LocalDateTime.now());
        spec = spec.and(((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("eventDate"), start)));

        if (rangeEnd != null) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("eventDate"), rangeEnd)));
        }

        if (onlyAvailable != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0));
        }

        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), Status.PUBLISHED));
        return spec;
    }

    private Map<Long, Long> getViews(List<Event> events) {
        if (events.isEmpty()) {
            return Collections.emptyMap();
        }

        LocalDateTime earliest = events.stream()
                .map(Event::getEventDate)
                .min(LocalDateTime::compareTo)
                .orElseThrow(() -> new IllegalStateException("Couldn't find the minimum date"));
        List<String> uris = events.stream()
                .map(event -> String.format("/events/%s", event.getId()))
                .toList();

        try {
            ResponseEntity<Object> responseEntity = clientStats.getStats(
                    LocalDateTime.now(),
                    earliest,
                    uris,
                    true
            );

            List<RequestHitsOutDto> viewList = objectMapper.convertValue(
                    responseEntity.getBody(),
                    new TypeReference<>() {}
            );

            return viewList.stream()
                    .filter(stats -> stats.getUri().startsWith("/events"))
                    .collect(Collectors.toMap(
                            stats -> Long.parseLong(stats.getUri().substring("/events/".length())),
                            RequestHitsOutDto::getHits
                    ));

        } catch (Exception e) {
            log.error("Error when getting page view statistics", e);
            return events.stream()
                    .collect(Collectors.toMap(
                            Event::getId,
                            event -> 0L
                    ));
        }
    }
        private void addClientStats(HttpServletRequest request) {
        clientStats.addRequest(RequestInDto.builder()
                .app(application)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
