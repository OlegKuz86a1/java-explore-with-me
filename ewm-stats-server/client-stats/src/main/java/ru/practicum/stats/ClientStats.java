package ru.practicum.stats;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.RequestInDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ClientStats extends  BaseClientStats {
    private static final String API_POSTFIX_POST = "/hit";
    private static final String API_POSTFIX_GET = "/stats?start={start}&end={end}&unique={unique}";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ClientStats(@Value("${ewm-stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public void addRequest(RequestInDto requestInDto) {
       post(API_POSTFIX_POST, requestInDto);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(this.formatter),
                "end", end.format(this.formatter),
                "uris", String.join(",", uris),
                "unique", unique);
        return get(API_POSTFIX_GET, parameters);
    }
}
