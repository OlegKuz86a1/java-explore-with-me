package ru.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.RequestHitsOutDto;
import ru.practicum.RequestInDto;
import ru.practicum.model.Request;
import ru.practicum.model.RequestHits;
import ru.practicum.model.RequestMapper;
import ru.practicum.repository.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {


    private final StatsRepository statsRepository;
    private final RequestMapper requestMapper;
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveRequest(RequestInDto requestInDto) {
        Request request = statsRepository.save(requestMapper.toEntity(requestInDto));
        log.info("added a new request: id={}, uri={}", request.getId(), request.getUri());
    }

    @Override
    public List<RequestHitsOutDto> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime encodedStartDateTime = encodeDateTime(start);
        LocalDateTime encodedEndDateTime = encodeDateTime(end);

        Stream<RequestHits> var10000 = statsRepository
                .getRequestHits(encodedStartDateTime, encodedEndDateTime, uris, unique).stream();
        RequestMapper var10001 = requestMapper;
        Objects.requireNonNull(var10001);
        return var10000
                .map(var10001::mapToRequestDto)
                .peek((request) -> log.info("Processed statistics for URI: {}", request.getUri()))
                .toList();
    }

    private LocalDateTime encodeDateTime(String date) {
        if (date != null && !date.trim().isEmpty()) {
            try {
                String decodedDate = URLDecoder.decode(date, StandardCharsets.UTF_8);
                LocalDateTime parsedDateTime = LocalDateTime.parse(decodedDate, DATE_TIME_FORMAT);
                log.info("Successfully decoded date: {}", parsedDateTime);
                return parsedDateTime;
            } catch (IllegalArgumentException e) {
                log.error("Error decoding date: {}, reason: {}", date, e.getMessage());
                throw new IllegalArgumentException("Invalid date format: " + date, e);
            }
        }
        return null;

    }

}
