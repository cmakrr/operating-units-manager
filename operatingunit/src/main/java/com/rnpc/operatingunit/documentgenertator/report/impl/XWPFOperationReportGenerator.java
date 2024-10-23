package com.rnpc.operatingunit.documentgenertator.report.impl;

import com.rnpc.operatingunit.documentgenertator.document.DocumentGenerator;
import com.rnpc.operatingunit.documentgenertator.report.OperationReportGenerator;
import com.rnpc.operatingunit.model.MedicalWorker;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.model.OperationFact;
import com.rnpc.operatingunit.model.OperationPlan;
import com.rnpc.operatingunit.model.OperationStepStatus;
import com.rnpc.operatingunit.model.Patient;
import com.rnpc.operatingunit.service.MedicalWorkerService;
import com.rnpc.operatingunit.util.DateFormattingUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.xwpf.usermodel.TableWidthType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class XWPFOperationReportGenerator implements OperationReportGenerator {
    private static final String[] GENERAL_TABLE_TITLE = {"Дата", "Операционный блок", "Название операции"};
    private static final String[] PATIENT_TABLE_TITLE_BIRTH_YEAR = {"ФИО", "Год рождения", "Палата"};
    private static final String[] PATIENT_TABLE_TITLE_AGE = {"ФИО", "Возраст", "Палата"};
    private static final String[] OPERATION_TABLE_TITLE = {"Время начала", "Время окончания", "Продолжительность", "Оператор", "Ассистент", "Трансфузиолог", "Инструменты"};
    private static final String[] OPERATION_TABLE_SUBTITLE = {"Операционный план", "Операционный факт"};
    private static final String[] STEPS_TABLE_TITLE = {"№", "Этап", "Время начала", "Время окончания", "Продолжительность", "Комментарий"};
    private static final String OPERATION_START = "СТАРТ ОПЕРАЦИИ";
    private static final String OPERATION_END = "КОНЕЦ ОПЕРАЦИИ";
    private static final String PATIENT = "Пациент";
    private static final String STEPS = "Этапы";
    private static final String REPORT_NAME = "%s %s.docx";
    private static final int MAX_FILE_NAME_LEN = 255;

    private final MedicalWorkerService medicalWorkerService;

    @Setter
    private DocumentGenerator documentGenerator;

    public String getReportName(Operation operation) {
        String date = operation.getDate().format(DateTimeFormatter.ISO_DATE);
        String name = operation.getOperationName();

        int nameLength = MAX_FILE_NAME_LEN - date.length() - REPORT_NAME.split("\\.")[1].length();
        String fileName = name.length() > nameLength ? name.substring(0, nameLength) : name;

        return String.format(REPORT_NAME, fileName, operation.getDate().format(DateTimeFormatter.ISO_DATE));
    }

    public InputStream generate(Operation operation) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            XWPFDocument document = documentGenerator.createA4FormatDocument();
            createTables(document, operation);
            documentGenerator.setParagraphsSpacingAfterToZero(document.getParagraphs());

            document.write(byteArrayOutputStream);

            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void createTables(XWPFDocument document, Operation operation) {
        createGeneralTable(document, operation);
        createPatientTable(document, operation);
        createOperationTable(document, operation);
        createStepsTable(document, operation);
    }

    private void createGeneralTable(XWPFDocument document, Operation operation) {
        XWPFTable table = document.createTable(3, 2);
        table.setWidthType(TableWidthType.PCT);
        documentGenerator.setColumnWidth(table, 0, 2600);

        populateGeneralTable(table, operation);
        document.createParagraph().createRun();
    }

    private void createPatientTable(XWPFDocument document, Operation operation) {
        documentGenerator.setBoldParagraph(document.createParagraph(), PATIENT);

        XWPFTable table = document.createTable(3, 2);
        table.setWidthType(TableWidthType.PCT);
        documentGenerator.setColumnWidth(table, 0, 2600);

        populatePatientTable(table, operation.getPatient());
        document.createParagraph().createRun();
    }

    private void createOperationTable(XWPFDocument document, Operation operation) {
        XWPFTable table = document.createTable(8, 3);
        table.setWidthType(TableWidthType.PCT);
        documentGenerator.setColumnWidth(table, 0, 2600);
        documentGenerator.setColumnWidth(table, 1, 6000);
        documentGenerator.setColumnWidth(table, 2, 6000);

        populateOperationTable(table, operation);
        document.createParagraph().createRun();
    }

    private void createStepsTable(XWPFDocument document, Operation operation) {
        documentGenerator.setBoldParagraph(document.createParagraph(), STEPS);

        Set<OperationStepStatus> steps = operation.getOperationFact().getSteps();
        XWPFTable table = document.createTable(steps.size() + 3, 6);
        table.setWidthType(TableWidthType.PCT);
        documentGenerator.setColumnWidth(table, 0, 300);
        documentGenerator.setColumnWidth(table, 1, 2300);
        documentGenerator.setColumnWidth(table, 2, 1500);
        documentGenerator.setColumnWidth(table, 3, 1500);
        documentGenerator.setColumnWidth(table, 3, 3000);

        populateStepsTable(table, operation.getOperationFact());
    }

    private void populateGeneralTable(XWPFTable generalTable, Operation operation) {
        documentGenerator.populateLeftTableTitle(generalTable, GENERAL_TABLE_TITLE);
        setGeneralInfo(generalTable, operation);
    }

    private void populatePatientTable(XWPFTable patientTable, Patient patient) {
        String[] titles = Objects.nonNull(patient.getBirthYear()) ? PATIENT_TABLE_TITLE_BIRTH_YEAR : PATIENT_TABLE_TITLE_AGE;
        documentGenerator.populateLeftTableTitle(patientTable, titles);
        setPatientInfo(patientTable, patient);
    }

    private void populateOperationTable(XWPFTable operationTable, Operation operation) {
        operationTable.getRow(0).getCell(0).getParagraphs().get(0).setSpacingAfter(0);
        documentGenerator.populateLeftTableTitleFromRow(operationTable, OPERATION_TABLE_TITLE, 1);
        documentGenerator.populateTableTitleFromColumn(operationTable, OPERATION_TABLE_SUBTITLE, 1);

        setOperationPlanInfo(operationTable, operation.getOperationPlan(), operation.getInstruments());
        setOperationFactInfo(operationTable, operation.getOperationFact(), operation.getOperationFact().getInstruments());

        documentGenerator.setColumnsContentToCenter(operationTable, 0, 1);
    }

    private void populateStepsTable(XWPFTable stepsTable, OperationFact operationFact) {
        documentGenerator.populateTableTitle(stepsTable, STEPS_TABLE_TITLE);
        setSteps(stepsTable, operationFact.getSteps(), operationFact.getStartTime(), operationFact.getEndTime());

        documentGenerator.setColumnsContentToCenter(stepsTable, 0, 0);
    }

    private void setGeneralInfo(XWPFTable generalTable, Operation operation) {
        String dateString = DateFormattingUtil.formatDate(operation.getDate());
        documentGenerator.setParagraph(generalTable.getRow(0).getCell(1).getParagraphs().get(0), dateString);
        documentGenerator.setParagraph(generalTable.getRow(1).getCell(1).getParagraphs().get(0), operation.getOperatingRoom().getName());
        documentGenerator.setParagraph(generalTable.getRow(2).getCell(1).getParagraphs().get(0), operation.getOperationName());
    }

    private void setPatientInfo(XWPFTable patientTable, Patient patient) {
        documentGenerator.setParagraph(patientTable.getRow(0).getCell(1).getParagraphs().get(0), patient.getFullName());
        documentGenerator.setParagraph(patientTable.getRow(2).getCell(1).getParagraphs().get(0), String.valueOf(patient.getRoomNumber()));

        if (Objects.nonNull(patient.getBirthYear())) {
            documentGenerator.setParagraph(patientTable.getRow(1).getCell(1).getParagraphs().get(0), String.valueOf(patient.getBirthYear().getYear()));
        } else if (patient.getAge() > 0) {
            documentGenerator.setParagraph(patientTable.getRow(1).getCell(1).getParagraphs().get(0), String.valueOf(patient.getAge()));
        }
    }

    private void setOperationPlanInfo(XWPFTable operationTable, OperationPlan operationPlan, String instruments) {
        setOperationInfo(operationTable, operationPlan.getStartTime(), operationPlan.getEndTime(), operationPlan.getOperator(), operationPlan.getAssistant(), operationPlan.getTransfusiologist(), instruments, 1);
    }

    private void setOperationFactInfo(XWPFTable operationTable, OperationFact operationFact, String instruments) {
        setOperationInfo(operationTable, operationFact.getStartTime(), operationFact.getEndTime(), operationFact.getOperator(), operationFact.getAssistant(), operationFact.getTransfusiologist(), instruments, 2);
    }

    private void setOperationInfo(XWPFTable table, LocalDateTime startTime, LocalDateTime endTime, MedicalWorker operator, MedicalWorker assistant, MedicalWorker transfusiologist, String instruments, int column) {
        String startTimeString = DateFormattingUtil.formatToHoursAndMinutes(startTime);
        documentGenerator.setParagraph(table.getRow(1).getCell(column).getParagraphs().get(0), startTimeString);
        if (Objects.nonNull(endTime)) {
            String endTimeString = DateFormattingUtil.formatToHoursAndMinutes(endTime);
            documentGenerator.setParagraph(table.getRow(2).getCell(column).getParagraphs().get(0), endTimeString);

            Duration duration = Duration.between(startTime, endTime);
            String durationString = DateFormattingUtil.formatRussianDuration(duration);
            documentGenerator.setParagraph(table.getRow(3).getCell(column).getParagraphs().get(0), durationString);
        }
        setMedicalWorkerParagraph(table.getRow(4).getCell(column).getParagraphs().get(0), operator);
        setMedicalWorkerParagraph(table.getRow(5).getCell(column).getParagraphs().get(0), assistant);
        setMedicalWorkerParagraph(table.getRow(6).getCell(column).getParagraphs().get(0), transfusiologist);
        documentGenerator.setParagraph(table.getRow(7).getCell(column).getParagraphs().get(0), instruments);
    }

    private void setMedicalWorkerParagraph(XWPFParagraph paragraph, MedicalWorker medicalWorker) {
        if (Objects.nonNull(medicalWorker)) {
            documentGenerator.setParagraph(paragraph, medicalWorkerService.getFullNameOrRole(medicalWorker));
        }
    }

    private void setSteps(XWPFTable table, Set<OperationStepStatus> steps, LocalDateTime startTime, LocalDateTime endTime) {
        List<OperationStepStatus> sortedSteps = steps.stream()
                .filter(step -> Objects.nonNull(step.getStartTime()))
                .sorted(Comparator.comparing(OperationStepStatus::getStartTime))
                .toList();

        setOperationStart(table.getRow(1), sortedSteps, startTime);

        AtomicInteger indexAtomic = new AtomicInteger(1);
        sortedSteps.forEach(step -> {
            int currentIndex = indexAtomic.incrementAndGet();
            setStep(step, table.getRow(currentIndex), currentIndex);
        });

        int lastRowIndex = indexAtomic.get() + 1;
        setOperationEnd(table.getRow(lastRowIndex), endTime, lastRowIndex);
    }

    private void setOperationStart(XWPFTableRow row, List<OperationStepStatus> steps, LocalDateTime startTime) {
        if (!CollectionUtils.isEmpty(steps)) {
            setOperationStart(row, steps.get(0).getStartTime(), startTime, 1);
        } else {
            setOperationStart(row, null, startTime, 1);
        }
    }

    private void setOperationStart(XWPFTableRow row, LocalDateTime stepTime, LocalDateTime startTime, int index) {
        documentGenerator.setParagraph(row.getCell(0).getParagraphs().get(0), String.valueOf(index));
        documentGenerator.setParagraph(row.getCell(1).getParagraphs().get(0), OPERATION_START);
        String startTimeString = DateFormattingUtil.formatToHoursMinutesAndSeconds(startTime);
        documentGenerator.setParagraph(row.getCell(2).getParagraphs().get(0), startTimeString);

        if (Objects.nonNull(stepTime)) {
            String stepTimeString = DateFormattingUtil.formatToHoursMinutesAndSeconds(stepTime);
            documentGenerator.setParagraph(row.getCell(3).getParagraphs().get(0), stepTimeString);

            Duration duration = Duration.between(startTime, stepTime);
            String durationString = DateFormattingUtil.formatRussianDuration(duration);
            documentGenerator.setParagraph(row.getCell(4).getParagraphs().get(0), durationString);
        }
    }

    private void setOperationEnd(XWPFTableRow row, LocalDateTime endTime, int index) {
        documentGenerator.setParagraph(row.getCell(0).getParagraphs().get(0), String.valueOf(index));
        documentGenerator.setParagraph(row.getCell(1).getParagraphs().get(0), OPERATION_END);

        if (Objects.nonNull(endTime)) {
            String endTimeString = DateFormattingUtil.formatToHoursMinutesAndSeconds(endTime);
            documentGenerator.setParagraph(row.getCell(3).getParagraphs().get(0), endTimeString);
        }
    }

    private void setStep(OperationStepStatus step, XWPFTableRow row, int index) {
        documentGenerator.setParagraph(row.getCell(0).getParagraphs().get(0), String.valueOf(index));
        documentGenerator.setParagraph(row.getCell(1).getParagraphs().get(0), step.getStep().getName().toUpperCase());

        if (Objects.nonNull(step.getStartTime())) {
            String startTimeString = DateFormattingUtil.formatToHoursMinutesAndSeconds(step.getStartTime());
            documentGenerator.setParagraph(row.getCell(2).getParagraphs().get(0), startTimeString);

            if (Objects.nonNull(step.getEndTime())) {
                String endTimeString = DateFormattingUtil.formatToHoursMinutesAndSeconds(step.getEndTime());
                documentGenerator.setParagraph(row.getCell(3).getParagraphs().get(0), endTimeString);

                Duration duration = Duration.between(step.getStartTime(), step.getEndTime());
                String durationString = DateFormattingUtil.formatRussianDuration(duration);
                documentGenerator.setParagraph(row.getCell(4).getParagraphs().get(0), durationString);
            }
            documentGenerator.setParagraph(row.getCell(5).getParagraphs().get(0), step.getComment());
        }
    }

}
