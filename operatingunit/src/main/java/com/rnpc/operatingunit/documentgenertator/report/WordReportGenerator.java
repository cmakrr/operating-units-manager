package com.rnpc.operatingunit.documentgenertator.report;

import com.rnpc.operatingunit.model.Operation;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.time.LocalDate;

public interface WordReportGenerator {
    WordprocessingMLPackage generateOperationReport(Operation operation);

    WordprocessingMLPackage generateReportByDate(LocalDate start, LocalDate end);
}
