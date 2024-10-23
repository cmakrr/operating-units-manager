package com.rnpc.operatingunit.documentgenertator.report.impl;

import com.rnpc.operatingunit.documentgenertator.document.DocumentGenerator;
import com.rnpc.operatingunit.documentgenertator.report.OperationsReportGenerator;
import com.rnpc.operatingunit.model.MedicalWorker;
import com.rnpc.operatingunit.model.OperatingRoom;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.model.OperationFact;
import com.rnpc.operatingunit.model.Patient;
import com.rnpc.operatingunit.service.MedicalWorkerService;
import com.rnpc.operatingunit.service.OperationService;
import com.rnpc.operatingunit.util.DateFormattingUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.xwpf.usermodel.TableWidthType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class XWPFOperationsReportGenerator implements OperationsReportGenerator {
    private static final String[] TABLE_TITLE = {"Время начала", "Время окончания", "ФИО пациента,\n возраст", "№ палаты", "Название операции", "Оператор,\n ассистент,\n трансфузиолог", "Материалы, инструменты"};
    private static final String REPORT_LINE = "Операционный отчет";
    private static final String REPORT_DATE_LINE = REPORT_LINE + " за %s";
    private static final String REPORT_DATES_LINE = REPORT_DATE_LINE + " - %s";
    private static final String PATIENT_COLUMN = "%s,\n %d";
    private static final String PATIENT_COLUMN_BIRTH_YEAR = PATIENT_COLUMN + " год";
    private static final String DOCX = ".docx";

    private final OperationService operationService;
    private final MedicalWorkerService medicalWorkerService;

    @Setter
    private DocumentGenerator documentGenerator;

    @Override
    public String getReportName(LocalDate start, LocalDate end) {
        if (Objects.nonNull(end) && !end.isEqual(start)) {
            return String.format(REPORT_DATES_LINE, start, end).concat(DOCX);
        } else {
            return String.format(REPORT_DATE_LINE, start).concat(DOCX);
        }
    }

    public InputStream generate(LocalDate start, LocalDate end) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            XWPFDocument document = documentGenerator.createA4FormatDocument();
            generateDocument(document, start, end);

            document.write(byteArrayOutputStream);

            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return InputStream.nullInputStream();
    }

    private void generateDocument(XWPFDocument document, LocalDate start, LocalDate end) {
        Map<LocalDate, Map<OperatingRoom, List<Operation>>> operations = operationService.getOngoingByDates(start, end);

        if (Objects.isNull(end) || start.isEqual(end)) {
            String title = String.format(REPORT_DATE_LINE, start);
            documentGenerator.setBoldParagraph(document.createParagraph(), title);

            generateReportForDay(document, operations.get(start));
        } else {
            String title = String.format(REPORT_DATES_LINE, start, end);
            documentGenerator.setBoldParagraph(document.createParagraph(), title);
            documentGenerator.setParagraph(document.createParagraph(), Strings.EMPTY);

            generateReportForDates(document, operations);
        }
    }

    private void generateReportForDay(XWPFDocument document, Map<OperatingRoom, List<Operation>> operationsMap) {
        int size = operationsMap.values().stream().mapToInt(List::size).sum() + operationsMap.keySet().size();
        XWPFTable table = createDayTable(document, size);
        populateDayTable(table, operationsMap);
    }

    private void generateReportForDates(XWPFDocument document, Map<LocalDate, Map<OperatingRoom, List<Operation>>> operationsMap) {
        Map<LocalDate, Map<OperatingRoom, List<Operation>>> finlterOperationsMap = filterOperationsMap(operationsMap);
        List<LocalDate> dates = finlterOperationsMap.keySet().stream().toList();

        IntStream.range(0, dates.size()).forEach(i -> {
            LocalDate date = dates.get(i);
            Map<OperatingRoom, List<Operation>> map = finlterOperationsMap.get(date);

            if (Objects.nonNull(map)) {
                String dateString = DateFormattingUtil.formatDate(date);
                documentGenerator.setBoldParagraph(document.createParagraph(), dateString);

                int size = map.values().stream().mapToInt(List::size).sum() + map.keySet().size();
                XWPFTable table = createDayTable(document, size);
                populateDayTable(table, map);

                if (i < finlterOperationsMap.size() - 1) {
                    documentGenerator.addPageBreak(document);
                }
            }
        });
    }

    private Map<LocalDate, Map<OperatingRoom, List<Operation>>> filterOperationsMap(Map<LocalDate, Map<OperatingRoom, List<Operation>>> operationsMap) {
        return operationsMap.entrySet().stream().map(entry -> {
            Map<OperatingRoom, List<Operation>> filteredInnerMap = entry.getValue().entrySet().stream().filter(innerEntry -> !innerEntry.getValue().isEmpty()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return Map.entry(entry.getKey(), filteredInnerMap);
        }).filter(entry -> !entry.getValue().isEmpty()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private XWPFTable createDayTable(XWPFDocument document, int rowCount) {
        XWPFTable table = document.createTable(rowCount + 1, 7);
        table.setWidthType(TableWidthType.PCT);

        return table;
    }

    private void populateDayTable(XWPFTable table, Map<OperatingRoom, List<Operation>> operationsMap) {
        documentGenerator.populateTableTitle(table, TABLE_TITLE);

        AtomicInteger i = new AtomicInteger(1);
        operationsMap.keySet().forEach(room -> {
            populateOperatingRoom(table, room, operationsMap.get(room), i.get());
            i.addAndGet(operationsMap.get(room).size() + 1);
        });

        documentGenerator.setColumnsContentToCenter(table, 0, 0);
    }

    private void populateOperatingRoom(XWPFTable table, OperatingRoom operatingRoom, List<Operation> operations, int startRow) {
        XWPFTableRow row = documentGenerator.mergeCellsHorizontally(table, startRow, 0, TABLE_TITLE.length - 1);
        documentGenerator.setParagraph(row.getCell(0).addParagraph(), Strings.EMPTY);
        documentGenerator.setBoldParagraph(row.getCell(0).getParagraphs().get(1), operatingRoom.getName());
        documentGenerator.setParagraph(row.getCell(0).addParagraph(), Strings.EMPTY);

        operations = operations.stream()
                .sorted(Comparator.comparing(op -> op.getOperationFact().getStartTime()))
                .toList();
        populateOperatingRoomOperations(table, operations, ++startRow);
    }

    private void populateOperatingRoomOperations(XWPFTable table, List<Operation> operations, int startRow) {
        IntStream.range(0, operations.size())
                .forEach(i -> populateOperation(table.getRow(startRow + i), operations.get(i)));

    }

    private void populateOperation(XWPFTableRow row, Operation operation) {
        OperationFact fact = operation.getOperationFact();
        List<MedicalWorker> workers = new ArrayList<>();
        workers.add(fact.getOperator());
        workers.add(fact.getAssistant());
        workers.add(fact.getTransfusiologist());

        String startTimeString = DateFormattingUtil.formatToHoursAndMinutes(fact.getStartTime());
        documentGenerator.setParagraph(row.getCell(0).getParagraphs().get(0), startTimeString);
        if (Objects.nonNull(fact.getEndTime())) {
            String endTimeString = DateFormattingUtil.formatToHoursAndMinutes(fact.getEndTime());
            documentGenerator.setParagraph(row.getCell(1).getParagraphs().get(0), endTimeString);
        }
        documentGenerator.setParagraph(row.getCell(2).getParagraphs().get(0), getPatientInfo(operation.getPatient()));
        documentGenerator.setParagraph(row.getCell(3).getParagraphs().get(0), String.valueOf(operation.getPatient().getRoomNumber()));
        documentGenerator.setParagraph(row.getCell(4).getParagraphs().get(0), operation.getOperationName());
        documentGenerator.setParagraph(row.getCell(5).getParagraphs().get(0), getMedicalWorkersInfo(workers));
        documentGenerator.setParagraph(row.getCell(6).getParagraphs().get(0), fact.getInstruments());
    }

    private String getPatientInfo(Patient patient) {
        String patientInfo = Strings.EMPTY;
        if (Objects.nonNull(patient.getBirthYear())) {
            patientInfo = String.format(PATIENT_COLUMN_BIRTH_YEAR, patient.getFullName(), patient.getBirthYear().getYear());
        } else if (patient.getAge() > 0) {
            patientInfo = String.format(PATIENT_COLUMN, patient.getFullName(), patient.getAge());
        }

        return patientInfo;
    }

    private String getMedicalWorkersInfo(List<MedicalWorker> medicalWorkers) {
        return medicalWorkers.stream()
                .filter(Objects::nonNull)
                .map(medicalWorkerService::getFullNameOrRole)
                .collect(Collectors.joining(",\n"));
    }

}
