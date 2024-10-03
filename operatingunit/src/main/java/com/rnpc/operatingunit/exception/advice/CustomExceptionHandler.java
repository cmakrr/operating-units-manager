package com.rnpc.operatingunit.exception.advice;

import com.rnpc.operatingunit.exception.operatingRoom.OperatingRoomUnauthorizedAction;
import com.rnpc.operatingunit.exception.operation.OperationFactCantBeFinishedException;
import com.rnpc.operatingunit.exception.operation.OperationFactStepNotFoundException;
import com.rnpc.operatingunit.exception.operation.OperationNotFoundException;
import com.rnpc.operatingunit.exception.operation.OperationFactCantBeCanceledException;
import com.rnpc.operatingunit.exception.operation.OperationFactNotCreatedException;
import com.rnpc.operatingunit.exception.operation.OperationFactNotStartException;
import com.rnpc.operatingunit.exception.operation.OperationFactStepCantBeCancelledException;
import com.rnpc.operatingunit.exception.response.ExceptionResponse;
import com.rnpc.operatingunit.exception.response.ValidationExceptionResponse;
import com.rnpc.operatingunit.exception.response.ViolationResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(OperatingRoomUnauthorizedAction.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleOperatingRoomUnauthorizedAction(OperatingRoomUnauthorizedAction exception) {
        return new ExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(OperationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleOperationNotFoundException(OperationNotFoundException exception) {
        return new ExceptionResponse(String.format("Операция с id = %d не была найдена", exception.getOperationId()));
    }

    @ExceptionHandler(OperationFactStepNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleOperationFactStepNotFoundException(OperationFactStepNotFoundException exception) {
        if (exception.isCurrentStep()) {
            new ExceptionResponse("Текущий шаг операции не найден");
        } else if (Objects.nonNull(exception.getOperationStepId()) && Objects.nonNull(exception.getOperationFactId())) {
            return new ExceptionResponse(
                    String.format("Операционный шаг с id = %d не был найден для операционного факта с id = %d",
                            exception.getOperationStepId(), exception.getOperationFactId()));
        }

        return new ExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(OperationFactNotCreatedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleOperationFactNotCreatedException(OperationFactNotCreatedException exception) {
        return new ExceptionResponse("Операционный факт еще не был создан");
    }

    @ExceptionHandler(OperationFactNotStartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleOperationFactNotStartException(OperationFactNotStartException exception) {
        return new ExceptionResponse(String.format("Операционный факт с id = %d еще не был начат",
                exception.getOperationFactId()));
    }

    @ExceptionHandler(OperationFactCantBeCanceledException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleOperationFactCantBeCanceledException(
            OperationFactCantBeCanceledException exception) {
        return new ExceptionResponse("Начало операции не может быть отменено");
    }

    @ExceptionHandler(OperationFactStepCantBeCancelledException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleOperationFactStepCantBeCancelledException(
            OperationFactStepCantBeCancelledException exception) {
        return new ExceptionResponse("Шаг операции не может быть отменен");
    }

    @ExceptionHandler(OperationFactCantBeFinishedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleOperationFactCantBeFinishedException(
            OperationFactCantBeFinishedException exception) {
        return new ExceptionResponse(String.format("Операционный факт с id = %d не может быть отменен",
                exception.getOperationFactId()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionResponse onConstraintValidationException(ConstraintViolationException exception) {
        final List<ViolationResponse> violations = exception.getConstraintViolations().stream()
                .map(violation -> new ViolationResponse(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());

        return new ValidationExceptionResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionResponse onMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final List<ViolationResponse> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ViolationResponse(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ValidationExceptionResponse(violations);
    }

}
