package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.enums.AccessRoleType;
import com.rnpc.operatingunit.model.AccessRole;

public interface AccessRoleService {
    AccessRole findByRole(AccessRoleType role);

    boolean existsByRole(AccessRoleType role);

    void save(AccessRole role);
}
