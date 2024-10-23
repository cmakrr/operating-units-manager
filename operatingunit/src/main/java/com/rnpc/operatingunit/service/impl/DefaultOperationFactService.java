package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.enums.MedicalWorkerOperationRole;
import com.rnpc.operatingunit.enums.OperationStepStatusName;
import com.rnpc.operatingunit.exception.operation.OperationFactCantBeCanceledException;
import com.rnpc.operatingunit.exception.operation.OperationFactCantBeFinishedException;
import com.rnpc.operatingunit.exception.operation.OperationFactNotCreatedException;
import com.rnpc.operatingunit.exception.operation.OperationFactNotStartException;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.model.OperationFact;
import com.rnpc.operatingunit.model.OperationPlan;
import com.rnpc.operatingunit.model.OperationStep;
import com.rnpc.operatingunit.model.OperationStepStatus;
import com.rnpc.operatingunit.repository.OperationFactRepository;
import com.rnpc.operatingunit.service.MedicalWorkerService;
import com.rnpc.operatingunit.service.OperationFactService;
import com.rnpc.operatingunit.service.OperationService;
import com.rnpc.operatingunit.service.OperationStepService;
import com.rnpc.operatingunit.service.OperationStepStatusService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DefaultOperationFactService implements OperationFactService {
    private final OperationFactRepository operationFactRepository;
    private final OperationService operationService;
    private final OperationStepStatusService operationStepStatusService;
    private final OperationStepService operationStepService;
    private final MedicalWorkerService medicalWorkerService;

    @Transactional
    public OperationFact createOperationFactForOperation(Long operationId) {
        OperationFact operationFact;
        Operation operation = operationService.getById(operationId);

        if (Objects.isNull(operation.getOperationFact())) {
            operationFact = createOperationFactFromOperation(operation.getOperationPlan());
            populateOperationFactSteps(operationFact);

            operationFactRepository.save(operationFact);

            operation.setDate(LocalDate.now());
            operationService.setOperationFact(operation, operationFact);

            return operationFact;
        } else {
            return operation.getOperationFact();
        }
    }

    @Transactional
    public OperationFact updateSettableInfo(Long operationFactId,
                                            Map<MedicalWorkerOperationRole, String> workers,
                                            String instruments) {
        OperationFact operationFact = getById(operationFactId);

        if (StringUtils.isNotBlank(instruments)) {
            operationFact.setInstruments(instruments.trim());
        }
        medicalWorkerService.saveOperationFactMedicalWorkers(operationFact, workers);

        return operationFactRepository.save(operationFact);
    }

    @Transactional
    public OperationFact start(Long operationId, Long operationFactId) {
        Operation operation = operationService.getById(operationId);
        OperationFact operationFact = getById(operationFactId);

        if (!isOperationFactStarted(operationFact)) {
            operationFact.setStartTime(LocalDateTime.now());
            operation.getOperatingRoom().setCurrentOperation(operation);

            operationService.save(operation);
        }

        return operationFact;
    }

    @Transactional
    public OperationFact cancelStart(Long operationId, Long operationFactId) {
        Operation operation = operationService.getById(operationId);
        OperationFact operationFact = getById(operationFactId);

        if (isOperationFactStarted(operationFact)) {
            if (!operationStepStatusService.isSomeStarted(operationFact.getSteps())) {
                populateCanceledOperationFact(operationFact);
                operation.getOperatingRoom().setCurrentOperation(null);

                return operationService.save(operation).getOperationFact();
            } else {
                throw new OperationFactCantBeCanceledException();
            }
        } else {
            throw new OperationFactNotStartException(operationFactId);
        }
    }

    @Transactional
    public OperationStepStatus startNextOperationStep(Long operationFactId) {
        OperationFact operationFact = getById(operationFactId);

        if (isOperationFactStarted(operationFact)) {
            return operationStepStatusService.proceedToNext(operationFact.getSteps());
        } else {
            throw new OperationFactNotStartException(operationFactId);
        }
    }

    public Optional<OperationStepStatus> getCurrentStep(Long operationFactId) {
        OperationFact operationFact = getById(operationFactId);

        if (isOperationFactStarted(operationFact)) {
            return Optional.of(operationStepStatusService.getCurrent(operationFact.getSteps()));
        } else {
           return Optional.empty();
        }
    }

    public OperationStepStatus cancelStep(Long operationFactId, Long stepId) {
        return operationStepStatusService.cansel(operationFactId, stepId);
    }

    @Transactional
    public OperationFact finish(Long operationId, Long operationFactId) {
        Operation operation = operationService.getById(operationId);
        OperationFact operationFact = getById(operationFactId);

        if (canOperationBeFinished(operationFact)) {
            operationFact.setEndTime(LocalDateTime.now());
            operation.getOperatingRoom().setCurrentOperation(null);

            operationStepStatusService.updateLastFinishedStep(operationFactId);

            return operationService.save(operation).getOperationFact();
        } else {
            throw new OperationFactCantBeFinishedException(operationFactId);
        }
    }

    private boolean canOperationBeFinished(OperationFact operationFact) {
        if (isOperationFactStarted(operationFact)) {
            return operationStepStatusService.areAllFinished(operationFact.getSteps());
        } else {
            return false;
        }
    }

    public OperationFact getById(Long operationFactId) {
        return operationFactRepository.findById(operationFactId).orElseThrow(OperationFactNotCreatedException::new);
    }

    private void populateCanceledOperationFact(OperationFact operationFact) {
        operationFact.setStartTime(null);
        operationFact.setOperator(null);
        operationFact.setAssistant(null);
        operationFact.setTransfusiologist(null);
        operationFact.setInstruments(null);
    }

    private OperationFact createOperationFactFromOperation(OperationPlan operationPlan) {
        OperationFact operationFact = new OperationFact();

        if (Objects.nonNull(operationPlan)) {
            operationFact.setOperator(operationPlan.getOperator());
            operationFact.setAssistant(operationPlan.getAssistant());
            operationFact.setTransfusiologist(operationPlan.getTransfusiologist());
        }

        return operationFact;
    }

    private void populateOperationFactSteps(OperationFact operationFact) {
        Set<OperationStepStatus> operationStepStatuses = new HashSet<>();
        Set<OperationStep> steps = operationStepService.getAll();

        steps.forEach(step -> {
            OperationStepStatus operationStepStatus = new OperationStepStatus();

            operationStepStatus.setStep(step);
            operationStepStatus.setOperationFact(operationFact);
            operationStepStatus.setStatus(OperationStepStatusName.NOT_STARTED);

            operationStepStatuses.add(operationStepStatus);
        });
        operationFact.setSteps(operationStepStatuses);
    }

    private boolean isOperationFactStarted(OperationFact operationFact) {
        return Objects.nonNull(operationFact.getStartTime());
    }

}
