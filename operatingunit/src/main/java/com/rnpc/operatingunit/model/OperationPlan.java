package com.rnpc.operatingunit.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
public class OperationPlan {
    @Id
    @Column(name = "op_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "op_start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "op_end_time", nullable = false)
    private LocalDateTime endTime;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "op_operator_id")
    private MedicalWorker operator;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "op_assistant_id")
    private MedicalWorker assistant;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "op_transfusiologist_id")
    private MedicalWorker transfusiologist;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        OperationPlan plan = (OperationPlan) obj;

        return Objects.equals(startTime, plan.getStartTime())
                && Objects.equals(endTime, plan.getEndTime())
                && Objects.equals(operator, plan.getOperator())
                && Objects.equals(assistant, plan.getAssistant())
                && Objects.equals(transfusiologist, plan.getTransfusiologist());
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, operator, assistant, transfusiologist);
    }

}
