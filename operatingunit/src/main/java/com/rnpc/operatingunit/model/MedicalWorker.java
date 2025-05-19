package com.rnpc.operatingunit.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

@Getter
@Setter
@Entity
@AttributeOverride(name = "id", column = @Column(name = "mw_id"))
@AttributeOverride(name = "fullName", column = @Column(name = "mw_full_name", unique = true))
public class MedicalWorker extends Person {
    @Column(name = "mw_role", unique = true)
    private String role;
    private String position;
    private WorkerStatus workerStatus = WorkerStatus.WORKING;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        MedicalWorker medicalWorker = (MedicalWorker) obj;

        return StringUtils.equalsIgnoreCase(this.getFullName(), medicalWorker.getFullName())
                && StringUtils.equalsIgnoreCase(role, medicalWorker.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.getFullName() != null ? this.getFullName().toLowerCase() : null,
                role != null ? role.toLowerCase() : null
        );
    }

}
