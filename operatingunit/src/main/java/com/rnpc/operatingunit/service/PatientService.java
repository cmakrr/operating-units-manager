package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.model.Patient;

public interface PatientService {
    Patient saveOrGetPatient(Patient patient);

    int getPatientAgeOrBirthYear(Patient patient);
}
