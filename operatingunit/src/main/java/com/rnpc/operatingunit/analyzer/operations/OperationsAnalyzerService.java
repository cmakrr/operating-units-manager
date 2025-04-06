package com.rnpc.operatingunit.analyzer.operations;

import com.rnpc.operatingunit.dto.response.operation.OperationInfo;
import com.rnpc.operatingunit.dto.response.operation.OperationsAnalysisInfo;
import com.rnpc.operatingunit.dto.response.operation.OperationsOverallAnalysis;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OperationsAnalyzerService {
    private final OperationRepository operationRepository;

    public OperationsOverallAnalysis createOperationsOverallAnalysis(LocalDate start, LocalDate end) {
        List<Operation> operations = operationRepository.findByDateBetween(start, end);
        return createOperationsOverallAnalysis(operations, start, end);
    }

    public OperationsAnalysisInfo createOperationsAnalysisInfo(LocalDate start, LocalDate end) {
        List<Operation> operations = operationRepository.findByDateBetween(start, end);
        return createOperationsAnalysisInfo(operations, start, end);
    }

    public OperationsOverallAnalysis createOperationsOverallAnalysis(List<Operation> operations, LocalDate start, LocalDate end) {
        OperationsOverallAnalysis overallAnalysis = new OperationsOverallAnalysis();

        int durationInMinutes = operations.stream()
                .mapToInt(this::calculateOperationsDurationInMinutes)
                .sum();
        overallAnalysis.setAllOperationsTimeInMinutes(durationInMinutes);

        int operationsCount = operations.size();
        overallAnalysis.setAllOperationsCount(operationsCount);
        int averageDuration = durationInMinutes / operationsCount;
        overallAnalysis.setAverageOperationDurationInMinutes(averageDuration);

        int analysisDaysRange = (int) (start.toEpochDay() - end.toEpochDay() + 1);
        float averageOperationsPerDay = ((float) operationsCount) / analysisDaysRange;
        overallAnalysis.setAverageOperationsPerDay(averageOperationsPerDay);
        int averageOperationMinutesPerDay = durationInMinutes / analysisDaysRange;
        overallAnalysis.setAverageOperationMinutesPerDay(averageOperationMinutesPerDay);

        int stepsCount = operations.stream()
                .mapToInt(operation -> operation.getOperationFact().getSteps().size())
                .sum();
        float averageStepsCount = ((float) stepsCount) / operationsCount;
        overallAnalysis.setAverageOperationSteps(averageStepsCount);

        return overallAnalysis;
    }

    public OperationsAnalysisInfo createOperationsAnalysisInfo(List<Operation> operations, LocalDate start, LocalDate end) {
        OperationsAnalysisInfo result = new OperationsAnalysisInfo();
        Map<LocalDate, List<OperationInfo>> operationsInfoByDate = new HashMap<>();

        int daysCount = (int) (start.toEpochDay() - end.toEpochDay());
        for (int i = 0; i < daysCount; i++) {
            LocalDate date = start.plusDays(i);
            operationsInfoByDate.put(date, new ArrayList<>());
        }

        for (Operation operation : operations) {
            List<OperationInfo> infoList = operationsInfoByDate.get(operation.getDate());
            OperationInfo info = createOperationInfo(operation);
            infoList.add(info);
        }

        result.setOperations(operationsInfoByDate);
        result.setAnalysisStart(start);
        result.setAnalysisEnd(end);
        return result;
    }

    private OperationInfo createOperationInfo(Operation operation) {
        return OperationInfo.builder()
                .operationDate(operation.getDate())
                .operationRoom(operation.getOperatingRoom().getName())
                .operationName(operation.getOperationName())
                .operationSteps(operation.getOperationFact().getSteps().size())
                .minutesDuration(calculateOperationsDurationInMinutes(operation))
                .build();
    }

    private int calculateOperationsDurationInMinutes(Operation operation) {
        LocalDateTime start = operation.getOperationFact().getStartTime();
        LocalDateTime end = operation.getOperationFact().getEndTime();
        return Duration.between(start, end).toMinutesPart();
    }
}
