package com.rnpc.operatingunit.dto.response.operation;

import com.rnpc.operatingunit.model.MedicalWorker;
import com.rnpc.operatingunit.model.OperatingRoom;
import lombok.Data;

import java.util.List;

@Data
public class OperationAvailableInfoResponse {
    private List<MedicalWorker> availableWorkers;
    private List<OperatingRoom> freeOperatingRooms;
}
