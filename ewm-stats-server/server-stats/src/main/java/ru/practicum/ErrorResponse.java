package ru.practicum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String error;
    private int status;
    private String message;
    private String path;

    public ErrorResponse(LocalDateTime timestamp, String error, int status, String message, String path) {
        this.timestamp = timestamp;
        this.error = error;
        this.status = status;
        this.message = message;
        this.path = path;
    }
}