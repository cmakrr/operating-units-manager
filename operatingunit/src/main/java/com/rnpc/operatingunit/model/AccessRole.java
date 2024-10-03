package com.rnpc.operatingunit.model;

import com.rnpc.operatingunit.enums.AccessRoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
public class AccessRole {
    @Id
    @Column(name = "ar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ar_role", nullable = false, unique = true)
    private AccessRoleType role;
    @Column(name = "ar_settable", nullable = false)
    private boolean isSettable;
    @Column(name = "ar_description", nullable = false)
    private String description;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        AccessRole accessRole = (AccessRole) obj;

        return Objects.equals(role, accessRole.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

}
