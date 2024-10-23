package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.enums.MedicalWorkerOperationRole;
import com.rnpc.operatingunit.model.MedicalWorker;
import com.rnpc.operatingunit.model.OperationFact;
import com.rnpc.operatingunit.model.OperationPlan;
import com.rnpc.operatingunit.repository.MedicalWorkerRepository;
import com.rnpc.operatingunit.service.MedicalWorkerService;
import com.rnpc.operatingunit.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultMedicalWorkerService implements MedicalWorkerService {
    private final MedicalWorkerRepository medicalWorkerRepository;
    private final PersonService personService;

    @Transactional
    public MedicalWorker saveOrGetMedicalWorker(MedicalWorker medicalWorker) {
        if (Objects.nonNull(medicalWorker) &&
                (Objects.nonNull(medicalWorker.getFullName()) || Objects.nonNull(medicalWorker.getRole()))) {
            Optional<MedicalWorker> worker =
                    medicalWorkerRepository.findByFullNameAndRole(medicalWorker.getFullName(), medicalWorker.getRole());
            if (worker.isPresent()) {
                medicalWorker = worker.get();
            } else {
                medicalWorker = medicalWorkerRepository.save(medicalWorker);
                log.info("Medical worker [{}] was saved", medicalWorker.getFullName());
            }
        }

        return medicalWorker;
    }

    @Transactional
    public Set<MedicalWorker> saveOperationFactMedicalWorkers(OperationFact operationFact,
                                                              Map<MedicalWorkerOperationRole, String> workers) {
        return workers.entrySet().stream()
                .map(worker -> saveOperationFactMedicalWorkerByRole(operationFact, worker.getValue(), worker.getKey()))
                .collect(Collectors.toSet());
    }

    @Transactional
    public MedicalWorker saveOperationFactMedicalWorkerByRole(OperationFact operationFact, String name,
                                                              MedicalWorkerOperationRole role) {
        MedicalWorker medicalWorker = getMedicalWorker(name);

        switch (role) {
            case OPERATOR -> operationFact.setOperator(saveOrGetMedicalWorker(medicalWorker));
            case ASSISTANT -> operationFact.setAssistant(saveOrGetMedicalWorker(medicalWorker));
            case TRANSFUSIOLOGIST -> operationFact.setTransfusiologist(saveOrGetMedicalWorker(medicalWorker));
        }

        return medicalWorker;
    }

    public String getFullNameOrRole(MedicalWorker medicalWorker) {
        String role = medicalWorker.getRole();
        String fullName = medicalWorker.getFullName();

        return StringUtils.isNotBlank(fullName) ? fullName : role;
    }

    public MedicalWorker setOperationPlanMedicalWorkerByRole(OperationPlan operationPlan, String name,
                                                             MedicalWorkerOperationRole role) {
        MedicalWorker medicalWorker = getMedicalWorker(name);

        switch (role) {
            case OPERATOR -> operationPlan.setOperator(medicalWorker);
            case ASSISTANT -> operationPlan.setAssistant(medicalWorker);
            case TRANSFUSIOLOGIST -> operationPlan.setTransfusiologist(medicalWorker);
        }

        return medicalWorker;
    }

    public Map<MedicalWorkerOperationRole, String> createMedicalWorkersRoleMap(String operatorName,
                                                                                String assistantName,
                                                                                String transfusiologistName) {
        Map<MedicalWorkerOperationRole, String> workers = new HashMap<>();
        workers.put(MedicalWorkerOperationRole.OPERATOR, operatorName);
        workers.put(MedicalWorkerOperationRole.ASSISTANT, assistantName);
        workers.put(MedicalWorkerOperationRole.TRANSFUSIOLOGIST, transfusiologistName);

        return workers;
    }

    private MedicalWorker getMedicalWorker(String name) {
        MedicalWorker medicalWorker = new MedicalWorker();

        if (StringUtils.isNotBlank(name)) {
            String fullName = personService.getFullName(name);

            if (personService.isFullName(fullName)) {
                medicalWorker.setFullName(fullName);
            } else {
                medicalWorker.setRole(fullName.toUpperCase());
            }
        }

        return medicalWorker;
    }

}
