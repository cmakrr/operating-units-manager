package com.rnpc.operatingunit.exception.operation;


import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class OperationFactStepNotFoundException extends RuntimeException {
    private Long operationFactId;
    private Long operationStepId;
    private boolean isCurrentStep;

    public OperationFactStepNotFoundException(String message) {
        super(message);
    }

    public OperationFactStepNotFoundException(Long operationFactId, Long operationStepId) {
        this.operationFactId = operationFactId;
        this.operationStepId = operationStepId;
    }

    public OperationFactStepNotFoundException(boolean isCurrentStep) {
        this.isCurrentStep = isCurrentStep;
    }

}
