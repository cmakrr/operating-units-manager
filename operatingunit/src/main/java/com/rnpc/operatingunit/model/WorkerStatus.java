package com.rnpc.operatingunit.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WorkerStatus {
    WORKING("working"),
    VACATIONING("vacationing"),
    SICK("sick");

    private final String status;
}
