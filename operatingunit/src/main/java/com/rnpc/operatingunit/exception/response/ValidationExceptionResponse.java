package com.rnpc.operatingunit.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationExceptionResponse {
    private final List<ViolationResponse> violations;
}
