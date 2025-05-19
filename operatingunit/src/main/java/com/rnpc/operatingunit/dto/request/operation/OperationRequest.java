package com.rnpc.operatingunit.dto.request.operation;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OperationRequest {
    private String operationName;
    private LocalDate date;
    private String instruments;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long operatorId;
    private Long assistantId;
    private Long transfusiologistId;
    private Long patientId;
    private Long operatingRoomId;
}
