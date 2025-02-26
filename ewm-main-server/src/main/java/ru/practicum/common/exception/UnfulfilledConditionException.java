package ru.practicum.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnfulfilledConditionException extends RuntimeException {

    public UnfulfilledConditionException(String message) {
        super(message);
        log.error(message);
    }
}
