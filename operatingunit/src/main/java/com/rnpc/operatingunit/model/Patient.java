package com.rnpc.operatingunit.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@AttributeOverride(name = "id", column = @Column(name = "p_id"))
@AttributeOverride(name = "fullName", column = @Column(name = "p_full_name", nullable = false))
public class Patient extends Person {
    @Column(name = "p_age")
    private int age;
    @Column(name = "p_birth_year")
    private LocalDate birthYear;
    @Column(name = "p_room_number")
    private int roomNumber;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        Patient patient = (Patient) obj;

        return StringUtils.equalsIgnoreCase(this.getFullName(), patient.getFullName())
                && Objects.equals(birthYear, patient.getBirthYear())
                && age == patient.getAge()
                && roomNumber == patient.getRoomNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.getFullName() != null ? this.getFullName().toLowerCase() : null,
                birthYear,
                age,
                roomNumber
        );
    }

}
