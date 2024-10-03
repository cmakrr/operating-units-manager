package com.rnpc.operatingunit.documentgenertator.report;

import com.rnpc.operatingunit.model.Operation;

import java.time.LocalDate;

public interface ReportFileGenerator {
    boolean generateOperationReport(String filePath, Operation operation);

    boolean generateReportByDate(String filePath, LocalDate start, LocalDate end);
}
