package com.rnpc.operatingunit.model;


import com.rnpc.operatingunit.enums.OperationStepStatusName;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "operation_fact_has_operation_step")
public class OperationStepStatus {
    @EmbeddedId
    @Column(name = "ofhos_id")
    private OperationStepStatusKey id = new OperationStepStatusKey();

    @Column(name = "ofhos_status")
    private OperationStepStatusName status;
    @Column(name = "ofhos_start_time")
    private LocalDateTime startTime;
    @Column(name = "ofhos_end_time")
    private LocalDateTime endTime;
    @Column(name = "ofhos_comment")
    private String comment;
    @Column(name = "ofhos_can_cancelled")
    private boolean canCancelled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("operationFactId")
    @JoinColumn(name = "ofhos_operation_fact_id")
    private OperationFact operationFact;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("stepId")
    @JoinColumn(name = "ofhos_operation_step_id")
    private OperationStep step;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        OperationStepStatus operationStepStatus = (OperationStepStatus) obj;

        return Objects.equals(operationFact, operationStepStatus.getOperationFact())
                && Objects.equals(step, operationStepStatus.getStep());
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationFact, step);
    }

}
