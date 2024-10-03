package com.rnpc.operatingunit.exception.operation;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OperationFactStepCantBeCancelledException extends RuntimeException {
    public OperationFactStepCantBeCancelledException(String message) {
        super(message);
    }
}
