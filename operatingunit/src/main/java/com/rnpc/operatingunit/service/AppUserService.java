package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.model.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AppUserService extends UserDetailsService {
    AppUser findById(Long id);

    AppUser findByLogin(String login);

    List<AppUser> getAll();

    List<AppUser> getByRole(String role);

    AppUser registerNewUser(AppUser appUser);

    void updatePassword(Long id, String password);

    void deleteById(Long id);

    void deleteByLogin(String login);

    AppUser getCurrentUser();
}
