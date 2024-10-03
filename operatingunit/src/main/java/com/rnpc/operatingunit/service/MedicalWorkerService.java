package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.enums.MedicalWorkerOperationRole;
import com.rnpc.operatingunit.model.MedicalWorker;
import com.rnpc.operatingunit.model.OperationFact;
import com.rnpc.operatingunit.model.OperationPlan;

import java.util.Map;
import java.util.Set;

public interface MedicalWorkerService {
    String getFullNameOrRole(MedicalWorker medicalWorker);

    MedicalWorker saveOrGetMedicalWorker(MedicalWorker medicalWorker);

    Set<MedicalWorker> saveOperationFactMedicalWorkers(OperationFact operationFact,
                                                       Map<MedicalWorkerOperationRole, String> workers);

    MedicalWorker saveOperationFactMedicalWorkerByRole(OperationFact operationFact, String name,
                                                       MedicalWorkerOperationRole role);

    MedicalWorker setOperationPlanMedicalWorkerByRole(OperationPlan operationPlan, String name,
                                                      MedicalWorkerOperationRole role);

    Map<MedicalWorkerOperationRole, String> createMedicalWorkersRoleMap(String operatorName, String assistantName,
                                                                        String transfusiologistName);

}