package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.MedicalWorker;
import com.rnpc.operatingunit.model.WorkerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicalWorkerRepository extends JpaRepository<MedicalWorker, Long> {
    Optional<MedicalWorker> findByFullNameAndRole(String fullName, String role);

    List<MedicalWorker> findByFullNameContaining(String name);

    Optional<MedicalWorker> findByFullName(String name);

    List<MedicalWorker> findByWorkerStatusNotAndFullNameIsNotNull(WorkerStatus workerStatus);

    @Query("SELECT mw FROM MedicalWorker mw WHERE NOT EXISTS (SELECT 1 FROM OperationPlan op " +
            "WHERE (op.operator.id = mw.id OR op.assistant.id = mw.id OR op.transfusiologist.id = mw.id)  " +
            "AND (op.startTime < :end AND op.endTime > :start))" )
    List<MedicalWorker> findFreeWorkers(LocalDateTime start, LocalDateTime end);

    List<MedicalWorker> findByWorkerStatus(WorkerStatus status);
}
