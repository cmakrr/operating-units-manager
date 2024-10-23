package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.enums.OperationStepStatusName;
import com.rnpc.operatingunit.exception.operation.OperationFactStepNotFoundException;
import com.rnpc.operatingunit.exception.operation.OperationFactStepCantBeCancelledException;
import com.rnpc.operatingunit.model.OperationStepStatus;
import com.rnpc.operatingunit.model.OperationStepStatusKey;
import com.rnpc.operatingunit.repository.OperationStepStatusRepository;
import com.rnpc.operatingunit.service.OperationStepStatusService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class DefaultOperationStepStatusService implements OperationStepStatusService {
    public static final OperationStepStatus EMPTY_STATUS = new OperationStepStatus();

    private final OperationStepStatusRepository operationStepStatusRepository;

    @Transactional
    public OperationStepStatus proceedToNext(Collection<OperationStepStatus> steps) {
        OperationStepStatus currentStep = new OperationStepStatus();
        List<OperationStepStatus> unfinishedSteps = getOperationUnfinishedSteps(steps).stream()
                .sorted(Comparator.comparing(st -> st.getStep().getStepNumber()))
                .toList();

        if (!CollectionUtils.isEmpty(unfinishedSteps)) {
            currentStep = unfinishedSteps.get(0);

            if (Objects.isNull(currentStep.getStartTime())) {
                populateNextStep(currentStep);

                currentStep = operationStepStatusRepository.save(currentStep);
            } else {
                currentStep = proceedOperation(currentStep, unfinishedSteps);
            }
        }

        return currentStep;
    }

    @Override
    public OperationStepStatus getCurrent(Collection<OperationStepStatus> steps) {
        return steps.stream()
                .filter(st -> Objects.isNull(st.getEndTime())
                        && Objects.nonNull(st.getStartTime())
                        && OperationStepStatusName.STARTED.equals(st.getStatus()))
                .min(Comparator.comparing(OperationStepStatus::getStartTime))
                .orElse(EMPTY_STATUS);
    }

    public boolean areAllFinished(Collection<OperationStepStatus> steps) {
        return steps.stream()
                .allMatch(st ->
                        Objects.nonNull(st.getEndTime())
                                && OperationStepStatusName.FINISHED.equals(st.getStatus())
                );
    }

    public boolean isSomeStarted(Collection<OperationStepStatus> steps) {
        return steps.stream()
                .anyMatch(step -> OperationStepStatusName.STARTED.equals(step.getStatus()));
    }

    public OperationStepStatus updateLastFinishedStep(Long operationFactId) {
        OperationStepStatus step = getLastFinishedStep(operationFactId);
        if (Objects.nonNull(step)) {
            step.setCanCancelled(false);
        }

        return step;
    }

    public List<OperationStepStatus> getOperationUnfinishedSteps(Collection<OperationStepStatus> steps) {
        return steps.stream()
                .filter(st -> Objects.isNull(st.getEndTime()))
                .filter(st -> !OperationStepStatusName.FINISHED.equals(st.getStatus()))
                .toList();
    }

    @Transactional
    public OperationStepStatus cansel(Long operationFactId, Long stepId) {
        OperationStepStatusKey stepStatusKey = new OperationStepStatusKey(operationFactId, stepId);
        Optional<OperationStepStatus> step = operationStepStatusRepository.findById(stepStatusKey);

        if (step.isPresent()) {
            OperationStepStatus cancelledStep = step.get();

            if (cancelledStep.isCanCancelled()) {
                OperationStepStatus newCurrentStep = getLastFinishedStep(operationFactId);

                if(!cancelledStep.equals(newCurrentStep)) {
                    populateCancelledStep(cancelledStep);
                }

                if (Objects.nonNull(newCurrentStep)) {
                    populateStepBeforeCancelled(newCurrentStep);
                    operationStepStatusRepository.saveAll(List.of(cancelledStep, newCurrentStep));
                } else {
                    operationStepStatusRepository.save(cancelledStep);
                }
            } else {
                throw new OperationFactStepCantBeCancelledException();
            }

            return cancelledStep;
        } else {
            throw new OperationFactStepNotFoundException(operationFactId, stepId);
        }
    }

    @Override
    public OperationStepStatus setComment(Long operationFactId, Long stepId, String comment) {
        OperationStepStatusKey stepStatusKey = new OperationStepStatusKey(operationFactId, stepId);
        Optional<OperationStepStatus> step = operationStepStatusRepository.findById(stepStatusKey);
        if (step.isPresent()) {
            OperationStepStatus stepStatus = step.get();
            stepStatus.setComment(comment.trim());

            return operationStepStatusRepository.save(stepStatus);
        } else {
            throw new OperationFactStepNotFoundException(operationFactId, stepId);
        }
    }

    @Nullable
    private OperationStepStatus getLastFinishedStep(Long operationFactId) {
        return operationStepStatusRepository
                .findFirstByOperationFact_IdAndEndTimeIsNotNullOrderByEndTimeDesc(operationFactId)
                .orElse(null);
    }

    private OperationStepStatus proceedOperation(OperationStepStatus step, List<OperationStepStatus> steps) {
        if (steps.size() > 1) {
            OperationStepStatus nextStep = steps.get(1);

            populateFinishedStep(step);
            populateNextStep(nextStep);
            operationStepStatusRepository.saveAll(List.of(step, nextStep));

            return nextStep;
        } else {
            populateLastFinishedStep(step);

            return operationStepStatusRepository.save(step);
        }
    }

    private void populateStep(OperationStepStatus step, Consumer<LocalDateTime> timeConsumer, LocalDateTime time,
                              OperationStepStatusName status, boolean canCancelled) {
        timeConsumer.accept(time);
        step.setStatus(status);
        step.setCanCancelled(canCancelled);
    }

    private void populateLastFinishedStep(OperationStepStatus step) {
        populateStep(step, step::setEndTime, LocalDateTime.now(), OperationStepStatusName.FINISHED, true);
    }

    private void populateFinishedStep(OperationStepStatus step) {
        populateStep(step, step::setEndTime, LocalDateTime.now(), OperationStepStatusName.FINISHED, false);
    }

    private void populateNextStep(OperationStepStatus step) {
        populateStep(step, step::setStartTime, LocalDateTime.now(), OperationStepStatusName.STARTED, true);
    }

    private void populateStepBeforeCancelled(OperationStepStatus step) {
        populateStep(step, step::setEndTime, null, OperationStepStatusName.STARTED, false);
    }

    private void populateCancelledStep(OperationStepStatus step) {
        populateStep(step, step::setStartTime, null, OperationStepStatusName.CANCELLED, step.isCanCancelled());
    }

}
