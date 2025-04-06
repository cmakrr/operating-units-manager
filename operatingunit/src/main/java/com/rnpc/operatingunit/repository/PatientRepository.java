package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByFullNameContaining(String name);
}
