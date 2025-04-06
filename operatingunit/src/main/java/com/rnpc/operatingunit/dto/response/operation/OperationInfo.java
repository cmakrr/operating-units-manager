package com.rnpc.operatingunit.dto.response.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperationInfo {
    private String operationRoom;
    private String operationName;
    private LocalDate operationDate;
    private int operationSteps;
    private int minutesDuration;
}
