package com.rnpc.operatingunit.documentgenertator.report;

import com.rnpc.operatingunit.documentgenertator.document.DocumentGenerator;

import java.io.InputStream;
import java.time.LocalDate;

public interface OperationsReportGenerator {
    void setDocumentGenerator(DocumentGenerator documentGenerator);

    InputStream generate(LocalDate start, LocalDate end);

    String getReportName(LocalDate start, LocalDate end);
}
