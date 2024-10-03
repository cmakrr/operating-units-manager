package com.rnpc.operatingunit.documentgenertator.report;

import com.rnpc.operatingunit.model.Operation;

import java.io.InputStream;
import java.time.LocalDate;

public interface ReportGenerator {
    InputStream generateOperationReport(Operation operation);

    String getOperationReportName(Operation operation);

    InputStream generateReportByDate(LocalDate start, LocalDate end);

    String getReportByDateName(LocalDate start, LocalDate end);
}
