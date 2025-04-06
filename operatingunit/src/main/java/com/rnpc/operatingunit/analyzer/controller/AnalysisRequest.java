package com.rnpc.operatingunit.analyzer.controller;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AnalysisRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}
