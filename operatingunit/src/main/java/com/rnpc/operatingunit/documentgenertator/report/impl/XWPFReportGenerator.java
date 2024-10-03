package com.rnpc.operatingunit.documentgenertator.report.impl;

import com.rnpc.operatingunit.documentgenertator.document.DocumentGenerator;
import com.rnpc.operatingunit.documentgenertator.report.OperationReportGenerator;
import com.rnpc.operatingunit.documentgenertator.report.OperationsReportGenerator;
import com.rnpc.operatingunit.documentgenertator.report.ReportGenerator;
import com.rnpc.operatingunit.model.Operation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class XWPFReportGenerator implements ReportGenerator {
    private static final String FONT = "Times New Roman";
    private static final int FONT_SIZE = 12;

    private final OperationsReportGenerator operationsReportGenerator;
    private final OperationReportGenerator operationReportGenerator;
    private final DocumentGenerator documentGenerator;

    @PostConstruct
    public void init() {
        documentGenerator.setFontStyle(FONT);
        documentGenerator.setFontSize(FONT_SIZE);

        operationReportGenerator.setDocumentGenerator(documentGenerator);
        operationsReportGenerator.setDocumentGenerator(documentGenerator);
    }

    public InputStream generateOperationReport(Operation operation) {
        return operationReportGenerator.generate(operation);
    }

    public String getOperationReportName(Operation operation) {
        return operationReportGenerator.getReportName(operation);
    }

    public InputStream generateReportByDate(LocalDate start, LocalDate end) {
        return operationsReportGenerator.generate(start, end);
    }

    public String getReportByDateName(LocalDate start, LocalDate end) {
        return operationsReportGenerator.getReportName(start, end);
    }

}
