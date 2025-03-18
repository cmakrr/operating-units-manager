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
    private int allOperationsDuration;
    private int allOperationsTimeInMinutes;
    private float averageOperationHoursPerDay;
    private float averageOperationSteps;
    private float averageOperationsPerDay;
}
