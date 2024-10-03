package com.rnpc.operatingunit.exception.advice;

import com.rnpc.operatingunit.exception.operatingRoom.OperatingRoomIpAddressDuplicateException;
import com.rnpc.operatingunit.exception.operatingRoom.OperatingRoomManageException;
import com.rnpc.operatingunit.exception.operatingRoom.OperatingRoomNameDuplicateException;
import com.rnpc.operatingunit.exception.response.ExceptionResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OperatingRoomExceptionHandler {
    @ExceptionHandler(OperatingRoomIpAddressDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleOperatingRoomIpAddressDuplicateException(
            OperatingRoomIpAddressDuplicateException exception) {
        String message = String.format("Ip-адрес %s не может быть назначен для %s! %s имеет данный ip-адрес.",
                exception.getIpAddress(),
                exception.getOperatingRoomNameWithInvalidIp(),
                exception.getOperatingRoomNameWithIp());

        return new ExceptionResponse(message);
    }

    @ExceptionHandler(OperatingRoomNameDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleOperatingRoomNameDuplicateException(OperatingRoomNameDuplicateException exception) {
        String message = String.format("Невозможно создать операционный блок с названием '%s'!" +
                        " Данное название имеет другой операционный блок медицинского учреждения.",
                exception.getName());

        return new ExceptionResponse(message);
    }

    @ExceptionHandler(OperatingRoomManageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleOperatingRoomManageException(OperatingRoomManageException exception) {
        String message = "Невозможно выполнить действие";
        String currentOperationName = exception.getCurrentOperationName();
        if (StringUtils.isNotBlank(currentOperationName)) {
            message = message.concat(String.format(", так как сейчас в операционном блоке " +
                    "проводится операция с названием '%s'! Повторите действие позже.", currentOperationName));
        }

        return new ExceptionResponse(message);
    }

}
