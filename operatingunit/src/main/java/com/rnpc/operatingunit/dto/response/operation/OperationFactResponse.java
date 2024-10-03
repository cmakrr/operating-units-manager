package com.rnpc.operatingunit.dto.response.operation;

import com.rnpc.operatingunit.dto.response.person.MedicalWorkerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationFactResponse {
    private Long id;
    private String startTime;
    private String endTime;
    private String instruments;
    private MedicalWorkerResponse operator;
    private MedicalWorkerResponse assistant;
    private MedicalWorkerResponse transfusiologist;
    private List<OperationStepStatusResponse> steps;
    private OperationStepStatusResponse currentStep;
}
