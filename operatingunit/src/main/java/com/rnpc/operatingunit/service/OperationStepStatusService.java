package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.model.OperationStepStatus;

import java.util.Collection;
import java.util.Set;

public interface OperationStepStatusService {
    OperationStepStatus proceedToNext(Collection<OperationStepStatus> steps);

    OperationStepStatus getCurrent(Collection<OperationStepStatus> steps);

    OperationStepStatus cansel(Long operationFactId, Long stepId);

    OperationStepStatus setComment(Long operationFactId, Long stepId, String comment);

    boolean areAllFinished(Collection<OperationStepStatus> steps);

    boolean isSomeStarted(Collection<OperationStepStatus> steps);

    OperationStepStatus updateLastFinishedStep(Long operationFactId);
}
