package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.MedicalWorker;
import com.rnpc.operatingunit.model.WorkerStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicalWorkerRepository extends JpaRepository<MedicalWorker, Long> {
    Optional<MedicalWorker> findByFullNameAndRole(String fullName, String role);

    List<MedicalWorker> findByWorkerStatus(WorkerStatus status);

    List<MedicalWorker> findByFullNameContaining(String name);

    @Query("SELECT medical_worker FROM medical_worker mw WHERE NOT EXISTS (SELECT 1 FROM operation_plan op " +
            "WHERE (op.op_operator_id = mw.id OR op.op_assistant_id = mw.id OR op.op_transfusiologist_id = mw.id)  " +
            "AND (op.op_start_time < :end AND op.op_end_time > :start))" )
    List<MedicalWorker> findFreeWorkers(LocalDateTime start, LocalDateTime end);
}
