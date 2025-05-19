package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.enums.PatientStatus;
import com.rnpc.operatingunit.model.Patient;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByFullNameContaining(String name);

    List<Patient> findByStatus(PatientStatus status);

    Optional<Patient> findByFullName(String name);

    @Query("SELECT p FROM Patient p WHERE p.status = 0 AND NOT EXISTS (SELECT 1 FROM Operation op " +
            "WHERE op.patient.id = p.id AND op.operationPlan.startTime < :end AND op.operationPlan.endTime > :start)" )
    List<Patient> findAvailablePatients(LocalDateTime start, LocalDateTime end);
}
