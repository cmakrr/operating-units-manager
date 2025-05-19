package com.rnpc.operatingunit.dto.request.operation;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationStepInfo {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String comment;
}
