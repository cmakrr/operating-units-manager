package com.rnpc.operatingunit.dto.response.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationStepStatusResponse {
    private String status;
    private String startTime;
    private String endTime;
    private String comment;
    private OperationStepResponse step;
    private boolean canCancelled;
}
