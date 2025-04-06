package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.model.Patient;

import java.util.List;

public interface PatientService {
    Patient saveOrGetPatient(Patient patient);

    void savePatient(Patient patient);

    Patient getPatient(Long id);

    List<Patient> findAll();

    List<Patient> findByFullNameContaining(String name);

    int getPatientAgeOrBirthYear(Patient patient);
}
