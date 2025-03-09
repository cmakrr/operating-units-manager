package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.enums.AccessRoleType;
import com.rnpc.operatingunit.exception.entity.EntityDuplicateException;
import com.rnpc.operatingunit.exception.entity.EntityNotFoundException;
import com.rnpc.operatingunit.exception.UnauthorizedAction;
import com.rnpc.operatingunit.model.AppUser;
import com.rnpc.operatingunit.repository.AccessRoleRepository;
import com.rnpc.operatingunit.repository.AppUserRepository;
import com.rnpc.operatingunit.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DefaultAppUserService implements AppUserService {
    private static final String USER_LOGIN_DUPLICATE_MESSAGE = "Пользователь с логином '%s' уже зарегистрирован!";
    private static final String UNAUTHORIZED_ACTION_MESSAGE =
            "Current user has insufficient role to manage user with role %s";
    private static final String NOT_FOUND_WITH = "Could not find user with ";
    private static final String NOT_FOUND_WITH_NAME = NOT_FOUND_WITH + "name %s";
    private static final String NOT_FOUND_WITH_ID = NOT_FOUND_WITH + "id %d";
    private static final String NOT_FOUND_WITH_LOGIN = NOT_FOUND_WITH + "login %s";

    private final AppUserRepository appUserRepository;
    private final AccessRoleRepository accessRoleRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) {
        return appUserRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND_WITH_NAME, username)));
    }

    @Override
    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_WITH_ID, id)));

    }

    @Override
    public AppUser findByLogin(String login) {
        return appUserRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_WITH_LOGIN, login)));
    }

    @Override
    public List<AppUser> getAll() {
        return appUserRepository.findAll();
    }

    @Override
    public List<AppUser> getByRole(String role) {
        boolean isRoleType = Arrays.stream(AccessRoleType.values())
                .anyMatch(type -> role.equalsIgnoreCase(type.getCode()));

        if (isRoleType) {
            return appUserRepository.findAllByRole_Role(AccessRoleType.valueOf(role.toUpperCase()));
        }

        return Collections.emptyList();
    }

    @Override
    public List<AppUser> getByRoleIn(List<String> roles) {
        List<AccessRoleType> roleTypes = roles.stream()
                .map(role->AccessRoleType.valueOf(role.toUpperCase()))
                .toList();
        return appUserRepository.findAllByRoleIn_RoleIn(roleTypes);
    }


    public AppUser registerTestUser(AppUser appUser){
        appUser.setRole(accessRoleRepository.findByRole(AccessRoleType.ADMIN).get());
        return saveNewUser(appUser);
    }

    @Override
    public AppUser registerNewUser(AppUser appUser) {
        if(appUser.getRole().getRole().getHierarchyPosition() > 0) {
            validateUserAccessToActionOn(appUser);
        }

        return saveNewUser(appUser);
    }

    @Override
    public void updatePassword(Long id, String password) {
        AppUser appUser = findById(id);
        validateUserAccessToActionOn(appUser);

        appUser.setPassword(password);
        encodeUserPassword(appUser);
        appUserRepository.save(appUser);
    }

    @Override
    public void deleteById(Long id) {
        AppUser appUser = findById(id);
        validateUserAccessToActionOn(appUser);

        appUserRepository.deleteById(id);
    }

    @Transactional
    public void deleteByLogin(String login) {
        appUserRepository.deleteByLogin(login);
    }

    @Override
    public AppUser getCurrentUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        return findByLogin(login);
    }

    private AppUser saveNewUser(AppUser user) {
        encodeUserPassword(user);

        return tryToSave(user);
    }

    private AppUser tryToSave(AppUser user) {
        String login = user.getLogin();
        if (!appUserRepository.existsByLogin(login)) {
            return appUserRepository.save(user);
        } else {
            throw new EntityDuplicateException(String.format(USER_LOGIN_DUPLICATE_MESSAGE, login));
        }
    }

    private void encodeUserPassword(AppUser user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    private void validateUserAccessToActionOn(AppUser appUser) {
        int currentHierarchyPosition = getAccessRoleTypeOfCurrentUser().getHierarchyPosition();
        AccessRoleType userRoleType = appUser.getRole().getRole();
        if (!isValidHierarchyPositionForAction(currentHierarchyPosition, userRoleType.getHierarchyPosition())) {
            throwUnauthorizedActionException(userRoleType.getCode());
        }
    }

    private AccessRoleType getAccessRoleTypeOfCurrentUser() {
        return getCurrentUser().getRole().getRole();
    }

    private boolean isValidHierarchyPositionForAction(int current, int another) {
        return current >= another;
    }

    private void throwUnauthorizedActionException(String role) {
        throw new UnauthorizedAction(String.format(UNAUTHORIZED_ACTION_MESSAGE, role));
    }

}
