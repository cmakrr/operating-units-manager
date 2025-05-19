package com.rnpc.operatingunit.controller;

import com.rnpc.operatingunit.dto.response.appUser.AppUserResponse;
import com.rnpc.operatingunit.service.AppUserService;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appUsers")
@RequiredArgsConstructor
public class AppUserController {
    private static final String INVALID_PASSWORD_SIZE_MESSAGE = "Пароль должен содердать от 3 до 255 символов!";

    private final ModelMapper modelMapper;
    private final AppUserService appUserService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<AppUserResponse> getAllUsers() {
        return modelMapper.map(appUserService.getAll(), new TypeToken<List<AppUserResponse>>() {
        }.getType());
    }

    @GetMapping("{role}")
    @ResponseStatus(HttpStatus.OK)
    public List<AppUserResponse> getUsersByRole(@PathVariable @NotBlank String role) {
        return modelMapper.map(appUserService.getByRole(role), new TypeToken<List<AppUserResponse>>() {
        }.getType());
    }

    @PutMapping("{appUserId}")
    @ResponseStatus(HttpStatus.OK)
    public void changeUserPassword(@PathVariable @Nonnull Long appUserId,
                                   @RequestBody @NotBlank
                                   @Size(min = 3, max = 255, message = INVALID_PASSWORD_SIZE_MESSAGE) String password) {
        appUserService.updatePassword(appUserId, password);
    }

    @DeleteMapping("{appUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppUser(@PathVariable Long appUserId) {
        appUserService.deleteById(appUserId);
    }
}
