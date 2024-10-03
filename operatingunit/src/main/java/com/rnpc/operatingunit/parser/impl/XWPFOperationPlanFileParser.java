package com.rnpc.operatingunit.parser.impl;

import com.rnpc.operatingunit.exception.file.NotSupportedFileExtensionException;
import com.rnpc.operatingunit.exception.plan.InvalidOperationPlanDateException;
import com.rnpc.operatingunit.exception.plan.MissingPlanColumnsException;
import com.rnpc.operatingunit.exception.plan.OperationPlanDateNotSetException;
import com.rnpc.operatingunit.exception.plan.OperationPlanParseException;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.parser.OperationPlanParser;
import com.rnpc.operatingunit.parser.OperationPlanTableParser;
import com.rnpc.operatingunit.service.FileConverter;
import com.rnpc.operatingunit.service.FileHandler;
import com.spire.doc.FileFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.DocumentIdentifiers.FINAL_OPERATION_PLAN;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.FileType.DOCX_FILE_TYPE;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.FileType.DOC_FILE_TYPE;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Formatter.DATE_FORMATTER;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Regexp.DATE_REGEX;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Regexp.SPACE_CHARACTER_REGEX;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Symbols.WHITESPACE;

@Component
@RequiredArgsConstructor
@Slf4j
public class XWPFOperationPlanFileParser implements OperationPlanParser {
    private static final Pattern datePattern = Pattern.compile(DATE_REGEX);

    private final FileConverter fileConverter;
    private final FileHandler fileHandler;
    private final OperationPlanTableParser operationPlanTableParser;

    @Transactional
    public List<Operation> parse(MultipartFile planFile) {
        try (InputStream inputStream = getFileInputStream(planFile)) {
            return tryParseOperationsPlanDocument(new XWPFDocument(inputStream));
        } catch (IOException e) {
            log.error("Failed to parse operation plan file {}: {}", planFile.getOriginalFilename(), e.getMessage(), e);
            throw new OperationPlanParseException();
        }
    }

    private InputStream getFileInputStream(MultipartFile planFile) {
        try {
            if (!DOCX_FILE_TYPE.equalsIgnoreCase(planFile.getContentType())) {
                String contentType = planFile.getContentType();
                if (StringUtils.isNotBlank(contentType) && DOC_FILE_TYPE.equals(contentType)) {
                    return fileConverter.convertToInputStream(fileConverter.convertToDocx(planFile, FileFormat.Doc));
                } else {
                    throw new NotSupportedFileExtensionException(
                            fileHandler.getFileExtension(planFile.getOriginalFilename()).orElse(Strings.EMPTY));
                }
            }

            return planFile.getInputStream();
        } catch (IOException e) {
            log.error("Error processing operation plan file {}: {}", planFile.getOriginalFilename(), e.getMessage());
            throw new OperationPlanParseException();
        }
    }

    private List<Operation> tryParseOperationsPlanDocument(XWPFDocument document) {
        List<Operation> operations = new ArrayList<>();
        LocalDate operationDate = tryParseOperationsPlanDate(document);
        for (XWPFTable table : document.getTables()) {
            try {
                operations.addAll(operationPlanTableParser.parse(table, operationDate));
            } catch (MissingPlanColumnsException e) {
                log.warn("Skipping the table in the operation plan: {}", e.getMessage());
            }
        }

        return operations;
    }

    private LocalDate tryParseOperationsPlanDate(XWPFDocument document) {
        Optional<LocalDate> date = getPlanDate(document.getParagraphs());
        if (date.isPresent()) {
            LocalDate planDate = date.get();
            if (!planDate.isBefore(LocalDate.now())) {
                return planDate;
            } else {
                throw new InvalidOperationPlanDateException(planDate, LocalDate.now());
            }
        }

        throw new OperationPlanDateNotSetException();
    }

    private Optional<LocalDate> getPlanDate(List<XWPFParagraph> paragraphs) {
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getParagraphText();
            if (text.replaceAll(SPACE_CHARACTER_REGEX, WHITESPACE).toLowerCase().contains(FINAL_OPERATION_PLAN)) {
                Matcher dateMatcher = datePattern.matcher(text);
                if (dateMatcher.find()) {
                    LocalDate date = LocalDate.parse(dateMatcher.group(1), DATE_FORMATTER);

                    return Optional.of(date);
                }
            }
        }

        return Optional.empty();
    }

}
