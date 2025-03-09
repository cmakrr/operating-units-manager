package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.enums.AccessRoleType;
import com.rnpc.operatingunit.model.AccessRole;
import com.rnpc.operatingunit.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByLogin(String login);

    Optional<AppUser> findByLogin(String login);

    List<AppUser> findAllByRoleIn_RoleIn(List<AccessRoleType> roles);

    List<AppUser> findAllByRole_Role(AccessRoleType role_role);

    void deleteByLogin(String login);
}
