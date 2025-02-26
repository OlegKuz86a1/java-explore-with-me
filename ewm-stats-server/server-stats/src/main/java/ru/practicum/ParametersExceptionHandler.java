package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

public class ParametersExceptionHandler {
    @ExceptionHandler(IncorrectParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameters(IncorrectParametersException ex) {
        return new ErrorResponse(
                LocalDateTime.now(),
                "Bad Request",
                400,
                ex.getMessage(),
                "/stats"
        );
    }
}
