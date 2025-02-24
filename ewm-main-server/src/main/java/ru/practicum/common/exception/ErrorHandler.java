package ru.practicum.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({UnfulfilledConditionException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUnfulfilledConditionException(UnfulfilledConditionException exception) {
        log.error("Unfulfilled condition error: {}", exception.getMessage(), exception);
        return new ApiError(
                exception.getMessage(),
                "the conditions for the requested operation are not met",
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException exception) {
        log.error("Not found condition error: {}", exception.getMessage(),exception);
        return new ApiError(
                exception.getMessage(),
                "Object not found",
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({IncorrectParametersException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectParametersException(IncorrectParametersException exception) {
        log.error("Incorrect parameters condition error: {}", exception.getMessage(),exception);
        return new ApiError(
                exception.getMessage(),
                "Incorrect parameters",
                HttpStatus.BAD_REQUEST
        );
    }
}
