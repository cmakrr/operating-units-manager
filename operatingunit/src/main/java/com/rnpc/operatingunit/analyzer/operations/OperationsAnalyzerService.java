package com.rnpc.operatingunit.analyzer.operations;

import com.rnpc.operatingunit.dto.response.operation.OperationsOverallAnalysis;
import com.rnpc.operatingunit.dto.response.operation.OperationsAnalysisInfo;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationsAnalyzerService {
    private final OperationRepository operationRepository;

    public OperationsOverallAnalysis createOperationsOverallAnalysis(List<Operation> operations){

    }

    public OperationsAnalysisInfo createOperationsAnalysisInfo(List<Operation> operations, LocalDate start, LocalDate end){

    }
}
