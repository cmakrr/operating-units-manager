package com.rnpc.operatingunit.dto.response.operation;

import lombok.Data;

import java.util.List;

@Data
public class OperationAvailableInfoResponse {
    private List<MedicalWorkerInfoResponse> availableWorkers;
    private List<OperatingRoomInfoResponse> availableOperatingRooms;
    private List<PatientInfoResponse> availablePatients;
}
