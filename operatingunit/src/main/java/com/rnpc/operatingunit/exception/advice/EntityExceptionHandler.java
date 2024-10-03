package com.rnpc.operatingunit.exception.advice;

import com.rnpc.operatingunit.exception.entity.EntityDuplicateException;
import com.rnpc.operatingunit.exception.entity.EntityNotFoundException;
import com.rnpc.operatingunit.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleEntityNotFoundException(EntityNotFoundException exception) {
        return new ExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(EntityDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleEntityDuplicateException(EntityDuplicateException exception) {
        return new ExceptionResponse(exception.getMessage());
    }
}
