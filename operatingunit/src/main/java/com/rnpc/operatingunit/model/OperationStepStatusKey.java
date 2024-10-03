package com.rnpc.operatingunit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OperationStepStatusKey implements Serializable {
    @Column(name = "ofhos_operation_fact_id")
    private Long operationFactId;
    @Column(name = "ofhos_operation_step_id")
    private Long stepId;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        OperationStepStatusKey operationStepStatusKey = (OperationStepStatusKey) obj;

        return Objects.equals(operationFactId, operationStepStatusKey.getOperationFactId())
                && Objects.equals(stepId, operationStepStatusKey.getStepId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationFactId, stepId);
    }

}
