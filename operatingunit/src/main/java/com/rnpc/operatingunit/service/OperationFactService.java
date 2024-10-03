package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.enums.MedicalWorkerOperationRole;
import com.rnpc.operatingunit.model.OperationFact;
import com.rnpc.operatingunit.model.OperationStepStatus;

import java.util.Map;

public interface OperationFactService {
    OperationFact getById(Long operationFactId);

    OperationFact createOperationFactForOperation(Long operationId);

    OperationFact updateSettableInfo(Long operationFactId, Map<MedicalWorkerOperationRole, String> workers,
                                     String instruments);

    OperationFact start(Long operationId, Long operationFactId);

    OperationFact cancelStart(Long operationId, Long operationFactId);

    OperationStepStatus getCurrentStep(Long operationFactId);

    OperationStepStatus cancelStep(Long operationFactId, Long stepId);

    OperationStepStatus startNextOperationStep(Long operationFactId);

    OperationFact finish(Long operationId, Long operationFactId);
}
