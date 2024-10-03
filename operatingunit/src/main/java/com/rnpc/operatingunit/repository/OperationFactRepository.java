package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.OperationFact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationFactRepository extends JpaRepository<OperationFact, Long> {
}
