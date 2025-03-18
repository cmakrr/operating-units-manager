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
public class OperationAnalysisInfo {
    private String operationRoom;
    private LocalDate operationDate;
    private int operationSteps;
    private int minutesDuration;
}
