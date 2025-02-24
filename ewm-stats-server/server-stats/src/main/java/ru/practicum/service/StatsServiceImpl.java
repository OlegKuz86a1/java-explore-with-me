package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.IncorrectParametersException;
import ru.practicum.RequestHitsOutDto;
import ru.practicum.RequestInDto;
import ru.practicum.model.Request;
import ru.practicum.model.RequestMapper;
import ru.practicum.repository.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsRepository statsRepository;
    private final RequestMapper requestMapper;

    @Override
    public void saveRequest(RequestInDto requestInDto) {
        Request request = statsRepository.save(requestMapper.toEntity(requestInDto));
        log.info("added a new request: id={}, uri={}", request.getId(), request.getUri());
    }

    @Override
    public List<RequestHitsOutDto> getStats(String start, String end, List<String> uris, boolean unique) {

        LocalDateTime startDate = start != null ? mapToLocalDateTime(start) : null;
        LocalDateTime endDate = end != null ? mapToLocalDateTime(end) : null;


        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            log.info("Uncorrected format of dates start {} и end {}", start, end);
            throw new IncorrectParametersException("Uncorrected format of dates");
        }

        return statsRepository
                .getRequestHits(startDate, endDate, uris, unique)
                .stream()
                .map(requestMapper::mapToRequestDto)
                .peek(request -> log.info("Processed statistics for URI: {}", request.getUri()))
                .toList();
    }

    private LocalDateTime mapToLocalDateTime(String date) {

        try {
            String decodedDate = URLDecoder.decode(date, StandardCharsets.UTF_8);
            LocalDateTime parsedDateTime = LocalDateTime.parse(decodedDate, DATE_TIME);
            log.info("Successfully decoded date: {}", parsedDateTime);
            return parsedDateTime;

        } catch (InvalidParameterException | DateTimeParseException e) {
            log.error("Error decoding date: {}, reason: {}", date, e.getMessage());
            throw new InvalidParameterException("Invalid date format: " + date, e);
        }
    }
}
