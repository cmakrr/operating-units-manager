package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
