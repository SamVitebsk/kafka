package ru.samusev.producer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.samusev.producer.domain.ErrorDto;

@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorDto runtimeExceptionHandler(RuntimeException ex) {
        log.error("RuntimeException: {}", ex.getMessage(), ex);
        return new ErrorDto(ex.getMessage());
    }
}
