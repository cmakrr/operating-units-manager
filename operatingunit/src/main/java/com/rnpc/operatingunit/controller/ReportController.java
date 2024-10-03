package com.rnpc.operatingunit.controller;

import com.rnpc.operatingunit.documentgenertator.report.ReportGenerator;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.service.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/report")
@RequiredArgsConstructor
public class ReportController {
    private static final String HEADER_SUGGESTED_FILENAME = "X-Suggested-Filename";
    private static final String HEADER_DOCX_CONTENT_TYPE
            = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    private final OperationService operationService;
    private final ReportGenerator reportGenerator;

    @SneakyThrows
    @GetMapping("/operations/{operationId}")
    public ResponseEntity<Resource> report(@PathVariable Long operationId) {
        Operation operation = operationService.getById(operationId);
        Resource file = new InputStreamResource(reportGenerator.generateOperationReport(operation));
        String encodedFileName = getEncodedFileName(reportGenerator.getOperationReportName(operation));

        return createResponseFromFile(encodedFileName, file);
    }

    @GetMapping("/operations/ongoing")
    public ResponseEntity<Resource> report(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (Objects.isNull(endDate)) {
            endDate = startDate;
        }

        Resource file = new InputStreamResource(reportGenerator.generateReportByDate(startDate, endDate));
        String encodedFileName = getEncodedFileName(reportGenerator.getReportByDateName(startDate, endDate));

        return createResponseFromFile(encodedFileName, file);
    }

    private String getEncodedFileName(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8);
    }

    private ResponseEntity<Resource> createResponseFromFile(String fileName, Resource file) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, HEADER_DOCX_CONTENT_TYPE)
                .header(HEADER_SUGGESTED_FILENAME, fileName)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(file);
    }
}
