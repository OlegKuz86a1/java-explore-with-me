package ru.practicum.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IncorrectParametersException extends RuntimeException {

    public IncorrectParametersException(String message) {
        super(message);
        log.error(message);
    }

}
