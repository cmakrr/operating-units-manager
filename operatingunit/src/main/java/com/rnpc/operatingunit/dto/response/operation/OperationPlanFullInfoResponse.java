package com.rnpc.operatingunit.dto.response.operation;

import com.rnpc.operatingunit.dto.response.person.PatientResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationPlanFullInfoResponse extends OperationPlanResponse {
    private String operationName;
    private String instruments;
    private PatientResponse patient;
    private Long operationId;
}
