package com.rnpc.operatingunit.exception;

public class UnauthorizedAction extends RuntimeException {
    public UnauthorizedAction(String message) {
        super(message);
    }
}
