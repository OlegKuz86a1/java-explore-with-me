package ru.practicum.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestHits {
    private String app;
    private String uri;
    private Long hits;

    public RequestHits(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
