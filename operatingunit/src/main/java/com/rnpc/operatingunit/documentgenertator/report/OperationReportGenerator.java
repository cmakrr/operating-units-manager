package com.rnpc.operatingunit.documentgenertator.report;

import com.rnpc.operatingunit.documentgenertator.document.DocumentGenerator;
import com.rnpc.operatingunit.model.Operation;

import java.io.InputStream;

public interface OperationReportGenerator {
    void setDocumentGenerator(DocumentGenerator documentGenerator);

    InputStream generate(Operation operation);

    String getReportName(Operation operation);
}
