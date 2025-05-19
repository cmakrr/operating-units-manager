package com.rnpc.operatingunit.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Operation {
    @Id
    @Column(name = "o_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "o_name", nullable = false)
    private String operationName;
    @Column(name = "o_date", nullable = false)
    private LocalDate date;
    @Column(name = "o_instruments")
    private String instruments;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "o_operatinn_plan_id", nullable = false)
    private OperationPlan operationPlan;
    @OneToOne
    @JoinColumn(name = "o_operation_fact_id")
    private OperationFact operationFact;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "o_patient_id", nullable = false)
    private Patient patient;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "o_operation_room_id", nullable = false)
    private OperatingRoom operatingRoom;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        Operation operation = (Operation) obj;

        return StringUtils.equalsIgnoreCase(operationName, operation.getOperationName())
                && StringUtils.equalsIgnoreCase(instruments, operation.getInstruments())
                && Objects.equals(date, operation.getDate())
                && Objects.equals(operationPlan, operation.getOperationPlan())
                && Objects.equals(patient, operation.getPatient())
                && Objects.equals(operatingRoom, operation.getOperatingRoom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                operationName != null ? operationName.toLowerCase() : null,
                instruments != null ? instruments.toLowerCase() : null,
                date,
                operationPlan,
                patient,
                operatingRoom
        );
    }

}
