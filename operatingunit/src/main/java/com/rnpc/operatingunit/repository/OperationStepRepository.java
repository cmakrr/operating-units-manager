package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.OperationStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationStepRepository extends JpaRepository<OperationStep, Long> {
}
