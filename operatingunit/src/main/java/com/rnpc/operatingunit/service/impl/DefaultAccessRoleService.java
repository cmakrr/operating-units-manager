package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.enums.AccessRoleType;
import com.rnpc.operatingunit.exception.entity.EntityNotFoundException;
import com.rnpc.operatingunit.model.AccessRole;
import com.rnpc.operatingunit.repository.AccessRoleRepository;
import com.rnpc.operatingunit.service.AccessRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAccessRoleService implements AccessRoleService {
    private final AccessRoleRepository accessRoleRepository;

    @Override
    public AccessRole findByRole(AccessRoleType role) {
        return accessRoleRepository.findByRole(role)
                .orElseThrow(()->{
                    String message = String.format("Cannot find role with role type %s", role);
                    return new EntityNotFoundException(message);
                });
    }

    @Override
    public boolean existsByRole(AccessRoleType role) {
        return accessRoleRepository.existsByRole(role);
    }

    @Override
    public void save(AccessRole role) {
        accessRoleRepository.save(role);
    }
}
