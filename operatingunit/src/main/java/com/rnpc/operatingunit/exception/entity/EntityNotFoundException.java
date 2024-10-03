package com.rnpc.operatingunit.exception.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
