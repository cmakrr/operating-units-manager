package com.rnpc.operatingunit.dto.response.operation;

import com.rnpc.operatingunit.dto.response.person.MedicalWorkerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationPlanResponse {
    private Long id;
    private String startTime;
    private String endTime;
    private MedicalWorkerResponse operator;
    private MedicalWorkerResponse assistant;
    private MedicalWorkerResponse transfusiologist;
}
