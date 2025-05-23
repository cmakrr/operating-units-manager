package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.model.Patient;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientService {
    Patient saveOrGetPatient(Patient patient);

    void savePatient(Patient patient);

    Patient getPatient(Long id);

    List<Patient> findAllPatientsInHospital();

    List<Patient> findAvailablePatients(LocalDateTime start, LocalDateTime end);

    List<Patient> findAll();

    List<Patient> findByFullNameContaining(String name);

    void dispatchPatient(Long id);

    int getPatientAgeOrBirthYear(Patient patient);
}
