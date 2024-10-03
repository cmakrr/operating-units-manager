package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.enums.AccessRoleType;
import com.rnpc.operatingunit.model.AccessRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessRoleRepository extends JpaRepository<AccessRole, Long> {
    Optional<AccessRole> findByRole(AccessRoleType role);

    boolean existsByRole(AccessRoleType role);
}
