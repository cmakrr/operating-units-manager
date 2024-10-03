package com.rnpc.operatingunit.parser;

import com.rnpc.operatingunit.model.Patient;

public interface PatientDetailsParser {
    void updatePatientFromDetails(Patient patient, String details);

    void setPatientRoomNumber(Patient patient, String roomNumberValue);
}
