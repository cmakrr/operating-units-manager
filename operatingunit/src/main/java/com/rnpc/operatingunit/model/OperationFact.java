package com.rnpc.operatingunit.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
public class OperationFact {
    @Id
    @Column(name = "of_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "of_start_time")
    private LocalDateTime startTime;
    @Column(name = "of_end_time")
    private LocalDateTime endTime;
    @Column(name = "of_instruments")
    private String instruments;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "of_operator_id")
    private MedicalWorker operator;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "of_assistant_id")
    private MedicalWorker assistant;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "of_transfusiologist_id")
    private MedicalWorker transfusiologist;
    @OneToMany(mappedBy = "operationFact", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OperationStepStatus> steps = new HashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        OperationFact operationFact = (OperationFact) obj;

        return Objects.equals(startTime, operationFact.getStartTime())
                && Objects.equals(endTime, operationFact.getEndTime())
                && Objects.equals(operator, operationFact.getOperator())
                && Objects.equals(assistant, operationFact.getAssistant())
                && Objects.equals(transfusiologist, operationFact.getTransfusiologist());
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, operator, assistant, transfusiologist);
    }

}
