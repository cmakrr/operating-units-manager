package com.rnpc.operatingunit.analyzer.workers;

import com.rnpc.operatingunit.analyzer.operations.OperationsAnalyzerService;
import com.rnpc.operatingunit.dto.response.operation.OperationsAnalysisInfo;
import com.rnpc.operatingunit.dto.response.operation.OperationsOverallAnalysis;
import com.rnpc.operatingunit.model.MedicalWorker;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.repository.MedicalWorkerRepository;
import com.rnpc.operatingunit.repository.OperationFactRepository;
import com.rnpc.operatingunit.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkersAnalyzerService {
    private final MedicalWorkerRepository medicalWorkerRepository;
    private final OperationsAnalyzerService operationsAnalyzerService;
    private final OperationFactRepository factRepository;
    private final OperationRepository operationRepository;

    public OperationsOverallAnalysis createOverallAnalysis(String workerName, String workerRole, LocalDate start, LocalDate end){
        List<Operation> operations = findWorkerOperations(workerName, workerRole, start, end);
        return operationsAnalyzerService.createOperationsOverallAnalysis(operations);
    }

    public OperationsAnalysisInfo createAnalysisInfo(String workerName, String workerRole, LocalDate start, LocalDate end){
        List<Operation> operations = findWorkerOperations(workerName, workerRole, start, end);
        return operationsAnalyzerService.createOperationsAnalysisInfo(operations, start, end);
    }

    private List<Operation> findWorkerOperations(String workerName, String workerRole, LocalDate start, LocalDate end){
        Optional<MedicalWorker> worker = medicalWorkerRepository.findByFullNameAndRole(workerName, workerRole);
        if(worker.isEmpty()){
            return null;
        }
        List<Long> operationIds =  factRepository.findIdsBetweenDatesWithWorker(start.atStartOfDay(), end.atTime(23,59), worker.get().getId());
        return operationRepository.findAllByIdIn(operationIds);
    }
}
