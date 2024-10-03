package com.rnpc.operatingunit.exception.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityDuplicateException extends RuntimeException {
    public EntityDuplicateException(String message) {
        super(message);
    }
}
