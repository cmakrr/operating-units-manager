package com.rnpc.operatingunit.exception.advice;

import com.rnpc.operatingunit.exception.file.NotSupportedFileExtensionException;
import com.rnpc.operatingunit.exception.plan.InvalidOperationPlanDateException;
import com.rnpc.operatingunit.exception.plan.OperatingRoomNotSetException;
import com.rnpc.operatingunit.exception.plan.OperationPlanCantBeModifiedException;
import com.rnpc.operatingunit.exception.plan.OperationPlanDateNotSetException;
import com.rnpc.operatingunit.exception.plan.OperationPlanParseException;
import com.rnpc.operatingunit.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class OperationPlanExceptionHandler {
    @ExceptionHandler(OperationPlanParseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleOperationPlanParseException(OperationPlanParseException e) {
        return new ExceptionResponse("Произошла ошибка при обработке операционного плана!");
    }

    @ExceptionHandler(NotSupportedFileExtensionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleNotSupportedFileExtensionException(NotSupportedFileExtensionException e) {
        String message = String.format("Формат %s файла не является поддерживаемым.",
                e.getNotSupportedExtension());
        return new ExceptionResponse(message);
    }

    @ExceptionHandler(OperationPlanDateNotSetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleOperationPlanDateNotSetException(OperationPlanDateNotSetException e) {
        return new ExceptionResponse("Дата в операционном плане не установлена!");
    }

    @ExceptionHandler(InvalidOperationPlanDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidOperationPlanDateException(InvalidOperationPlanDateException e) {
        return new ExceptionResponse(String.format("""
                        Операционный план на %s не может быть загружен.
                        Операциооный план может быть загружен для дат начиная с %s.
                        """, e.getInvalidDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                e.getFirstValidDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

    @ExceptionHandler(OperatingRoomNotSetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleOperatingRoomNotSetException(OperatingRoomNotSetException e) {
        return new ExceptionResponse("""
                Название одного из операционных блоков не установлено. Проверьте операционный план!
                """);
    }

    @ExceptionHandler(OperationPlanCantBeModifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleOperationPlanCantBeModifiedException(OperationPlanCantBeModifiedException e) {
        return new ExceptionResponse(String.format("""
                Операционный план на дату %s уже загружен и не может быть изменен! Обратитесь к администратору.
                """, e.getPlanDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }

}
