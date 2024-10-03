package com.rnpc.operatingunit.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ViolationResponse {
    private final String fieldName;
    private final String message;
}
