package com.rnpc.operatingunit.parser.impl;

import com.rnpc.operatingunit.enums.MedicalWorkerOperationRole;
import com.rnpc.operatingunit.enums.OperationInfoColumnName;
import com.rnpc.operatingunit.exception.plan.MissingPlanColumnsException;
import com.rnpc.operatingunit.exception.plan.OperatingRoomNotSetException;
import com.rnpc.operatingunit.model.OperatingRoom;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.model.OperationInfoColumn;
import com.rnpc.operatingunit.model.OperationPlan;
import com.rnpc.operatingunit.model.Patient;
import com.rnpc.operatingunit.parser.OperationPlanTableParser;
import com.rnpc.operatingunit.parser.PatientDetailsParser;
import com.rnpc.operatingunit.service.MedicalWorkerService;
import com.rnpc.operatingunit.service.OperationInfoColumnService;
import com.rnpc.operatingunit.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Formatter.TIME_FORMATTER;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Regexp.HYPHEN_ONE_OR_MORE_REGEX;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Regexp.SPACE_CHARACTER_REGEX;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Regexp.TIME_INTERVAL_REGEX;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Symbols.DOT;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Symbols.HYPHEN;
import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Symbols.WHITESPACE;

@Component
@RequiredArgsConstructor
public class XWPFOperationPlanTableParser implements OperationPlanTableParser {
    private final OperationService operationService;
    private final PatientDetailsParser patientDetailsParser;
    private final OperationInfoColumnService operationInfoColumnService;
    private final MedicalWorkerService medicalWorkerService;

    private List<MedicalWorkerOperationRole> medicalWorkerOperationRoles;

    public List<Operation> parse(XWPFTable table, LocalDate operationDate) throws MissingPlanColumnsException {
        List<OperationInfoColumn> columnNames = getHeaderColumnNames(table);
        if (!CollectionUtils.isEmpty(columnNames)) {
            setMedicalWorkerOperationRoles(columnNames);

            return tryParseOperationPlanTable(table, columnNames, operationDate);
        } else {
            throw new MissingPlanColumnsException();
        }
    }

    private List<OperationInfoColumn> getHeaderColumnNames(XWPFTable table) {
        List<OperationInfoColumn> availableColumns = operationInfoColumnService.getAll();

        List<String> tableHeaderNames = table.getRow(0).getTableCells().stream()
                .map(cell -> cell.getText().trim())
                .filter(StringUtils::isNotBlank)
                .map(String::toLowerCase)
                .toList();

        return availableColumns.stream()
                .filter(column -> tableHeaderNames.contains(column.getColumnName().toLowerCase()))
                .toList();
    }


    private void setMedicalWorkerOperationRoles(List<OperationInfoColumn> columns) {
        String medicalWorkersRole = getCurrentMedicalWorkerRoles(columns);
        medicalWorkerOperationRoles = Arrays.stream(medicalWorkersRole.split(","))
                .map(MedicalWorkerOperationRole::getByCode)
                .filter(Objects::nonNull)
                .toList();
    }

    private String getCurrentMedicalWorkerRoles(List<OperationInfoColumn> columns) {
        return columns.stream()
                .filter(name -> OperationInfoColumnName.MEDICAL_WORKERS.equals(name.getName()))
                .findAny()
                .map(OperationInfoColumn::getColumnName)
                .orElse(Strings.EMPTY);
    }

    private List<Operation> tryParseOperationPlanTable(XWPFTable table, List<OperationInfoColumn> orderedColumns,
                                                       LocalDate operationDate) {
        List<Operation> operations = new ArrayList<>();
        OperatingRoom room = new OperatingRoom();
        for (int rowIndex = 1; rowIndex < table.getRows().size(); rowIndex++) {
            XWPFTableRow row = table.getRow(rowIndex);
            List<XWPFTableCell> rowCells = row.getTableCells();
            int rowCellCount = Math.min(rowCells.size(), orderedColumns.size());

            if (rowCellCount == 1) {
                room = populateOperatingRoom(rowCells.get(0).getText());
            } else if (!isEmptyTableRow(row)) {
                Operation operation = createOperation(rowCellCount, room, operationDate, orderedColumns, rowCells);
                operations.add(operation);
            }
        }

        return operationService.saveAll(operations, operationDate);
    }

    private Operation createOperation(int rowCellCount, OperatingRoom room, LocalDate operationDate,
                                      List<OperationInfoColumn> orderedColumns, List<XWPFTableCell> rowCells) {
        Operation operation = createOperation(room, operationDate);

        for (int cellIndex = 0; cellIndex < rowCellCount; cellIndex++) {
            OperationInfoColumnName cellName = orderedColumns.get(cellIndex).getName();
            String cellValue = rowCells.get(cellIndex + 1).getText().trim();

            if (StringUtils.isNotBlank(cellValue)) {
                switch (cellName) {
                    case OPERATION_TIME_INTERVAL ->
                            populateOperationPlanTimeInterval(operation.getOperationPlan(), operationDate, cellValue);
                    case PATIENT_DETAILS ->
                            patientDetailsParser.updatePatientFromDetails(operation.getPatient(), cellValue);
                    case PATIENT_ROOM_NUMBER ->
                            patientDetailsParser.setPatientRoomNumber(operation.getPatient(), cellValue);
                    case OPERATION_NAME -> operation.setOperationName(cellValue);
                    case MEDICAL_WORKERS -> {
                        List<XWPFParagraph> medicalWorkers = rowCells.get(cellIndex + 1).getParagraphs();

                        setOperationPlanMedicalWorkers(operation.getOperationPlan(), medicalWorkers);
                    }
                    case INSTRUMENTS -> operation.setInstruments(cellValue);
                }
            }
        }

        return operation;
    }

    private boolean isEmptyTableRow(XWPFTableRow row) {
        return row.getTableCells().stream()
                .map(XWPFTableCell::getText)
                .allMatch(StringUtils::isBlank);
    }

    private Operation createOperation(OperatingRoom operatingRoom, LocalDate operationDate) {
        return Operation.builder()
                .patient(new Patient())
                .operationPlan(new OperationPlan())
                .operatingRoom(operatingRoom)
                .date(operationDate).build();
    }

    private OperatingRoom populateOperatingRoom(String name) {
        OperatingRoom operatingRoom = new OperatingRoom();
        String value = name.replaceAll(SPACE_CHARACTER_REGEX, WHITESPACE).trim();

        if (StringUtils.isNotBlank(value)) {
            int nameLastCharIndex = value.length() - 1;
            if (value.lastIndexOf(DOT) == nameLastCharIndex) {
                value = value.substring(0, nameLastCharIndex);
            }
            operatingRoom.setName(value);

            return operatingRoom;
        }

        throw new OperatingRoomNotSetException();
    }

    private void populateOperationPlanTimeInterval(OperationPlan operationPlan, LocalDate date, String timeInterval) {
        String[] timeValues = getTimeIntervalString(timeInterval);
        List<LocalDateTime> times = Arrays.stream(timeValues)
                .map(time -> date.atTime(LocalTime.parse(time, TIME_FORMATTER)))
                .toList();

        IntStream.range(0, times.size()).forEach(i -> {
            if (i == 0) {
                operationPlan.setStartTime(times.get(i));
            } else {
                operationPlan.setEndTime(times.get(i));
            }
        });
    }

    private String[] getTimeIntervalString(String timeInterval) {
        return timeInterval
                .replaceAll(TIME_INTERVAL_REGEX, Strings.EMPTY)
                .replaceAll(SPACE_CHARACTER_REGEX, HYPHEN)
                .replaceAll(HYPHEN_ONE_OR_MORE_REGEX, HYPHEN)
                .split(HYPHEN);
    }

    private void setOperationPlanMedicalWorkers(OperationPlan operationPlan, List<XWPFParagraph> workers) {
        IntStream.range(0, Math.min(medicalWorkerOperationRoles.size(), workers.size()))
                .forEach(i -> {
                    String worker = workers.get(i).getParagraphText();
                    MedicalWorkerOperationRole role = medicalWorkerOperationRoles.get(i);

                    medicalWorkerService.setOperationPlanMedicalWorkerByRole(operationPlan, worker, role);
                });
    }
}
