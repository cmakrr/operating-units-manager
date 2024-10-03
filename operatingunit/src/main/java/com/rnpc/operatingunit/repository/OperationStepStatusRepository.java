package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.OperationStepStatus;
import com.rnpc.operatingunit.model.OperationStepStatusKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationStepStatusRepository extends JpaRepository<OperationStepStatus, Long> {
    Optional<OperationStepStatus> findById(OperationStepStatusKey id);

    Optional<OperationStepStatus> findFirstByOperationFact_IdAndEndTimeIsNotNullOrderByEndTimeDesc(
            Long operationFactId);
}
