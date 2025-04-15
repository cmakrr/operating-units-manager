package com.rnpc.operatingunit.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WorkerStatus {
    WORKING("working"),
    VACATIONING("vacationing"),
    SICK("sick"),
    NOT_EMPLOYED("not_employed");

    private final String status;
}
