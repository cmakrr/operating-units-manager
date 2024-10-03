package com.rnpc.operatingunit.exception.operation;

public class OperationFactCantBeCanceledException extends RuntimeException {

    public OperationFactCantBeCanceledException() {
        super();
    }

    public OperationFactCantBeCanceledException(String message) {
        super(message);
    }
}
