package com.rnpc.operatingunit.dto.response.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OperationsOverallAnalysis {
    private int allOperationsCount;
    private int allOperationsTimeInMinutes;
    private int averageOperationDurationInMinutes;
    private int averageOperationMinutesPerDay;
    private float averageOperationSteps;
    private float averageOperationsPerDay;
}
