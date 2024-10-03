package com.rnpc.operatingunit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class OperationStep {
    @Id
    @Column(name = "os_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "os_number", nullable = false, unique = true)
    private int stepNumber;
    @Column(name = "os_name", nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "step", orphanRemoval = true)
    private List<OperationStepStatus> operationStepStatuses = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        OperationStep operationStep = (OperationStep) obj;

        return stepNumber == operationStep.getStepNumber()
                && StringUtils.equalsIgnoreCase(name, operationStep.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepNumber, name);
    }

}
