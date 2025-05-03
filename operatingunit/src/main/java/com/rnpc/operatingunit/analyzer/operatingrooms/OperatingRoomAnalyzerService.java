package com.rnpc.operatingunit.analyzer.operatingrooms;

import com.rnpc.operatingunit.analyzer.operations.OperationsAnalyzerService;
import com.rnpc.operatingunit.dto.response.operation.OperationsAnalysisInfo;
import com.rnpc.operatingunit.dto.response.operation.OperationsOverallAnalysis;
import com.rnpc.operatingunit.model.OperatingRoom;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.repository.OperatingRoomRepository;
import com.rnpc.operatingunit.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperatingRoomAnalyzerService {
    private final OperatingRoomRepository operatingRoomRepository;
    private final OperationsAnalyzerService operationsAnalyzerService;
    private final OperationRepository operationRepository;

    public OperationsOverallAnalysis createOverallAnalysis(String room, LocalDate start, LocalDate end){
        List<Operation> operations = findRoomOperations(room, start, end);
        return operationsAnalyzerService.createOperationsOverallAnalysis(operations, start, end);
    }

    public OperationsAnalysisInfo createAnalysisInfo(String room, LocalDate start, LocalDate end){
        List<Operation> operations = findRoomOperations(room, start, end);
        return operationsAnalyzerService.createOperationsAnalysisInfo(operations, start, end);
    }

    private List<Operation> findRoomOperations(String room, LocalDate start, LocalDate end){
        Optional<OperatingRoom> operatingRoom = operatingRoomRepository.findByNameIgnoreCase(room);
        if(operatingRoom.isEmpty()){
            return Collections.emptyList();
        }
        LocalDateTime startTime = start.atStartOfDay();
        LocalDateTime endTime = end.atTime(23,59);
        return operationRepository.findBetweenDatesInOperatingRoom(startTime, endTime, operatingRoom.get().getId());
    }
}
