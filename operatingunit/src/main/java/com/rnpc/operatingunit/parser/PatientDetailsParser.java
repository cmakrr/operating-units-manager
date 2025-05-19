package com.rnpc.operatingunit.parser;

import com.rnpc.operatingunit.model.Patient;

import java.util.Optional;

public interface PatientDetailsParser {
    void updatePatientFromDetails(Patient patient, String details);

    void setPatientRoomNumber(Patient patient, String roomNumberValue);

    Optional<Patient> tryFindByFullName(String fullName);
}
