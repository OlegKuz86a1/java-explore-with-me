package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectParametersException extends RuntimeException {

    public IncorrectParametersException(String message) {
        super(message);
    }

}
