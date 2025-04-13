package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.enums.PatientStatus;
import com.rnpc.operatingunit.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByFullNameContaining(String name);

    List<Patient> findByStatus(PatientStatus status);

    Optional<Patient> findByFullName(String name);
}
