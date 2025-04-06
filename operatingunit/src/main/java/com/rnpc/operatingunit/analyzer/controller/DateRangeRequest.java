package com.rnpc.operatingunit.analyzer.controller;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DateRangeRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}
