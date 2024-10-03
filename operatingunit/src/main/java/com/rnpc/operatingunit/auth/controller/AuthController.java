package com.rnpc.operatingunit.auth.controller;

import com.rnpc.operatingunit.auth.request.SignInRequest;
import com.rnpc.operatingunit.auth.request.SignUpRequest;
import com.rnpc.operatingunit.auth.response.JwtTokenResponse;
import com.rnpc.operatingunit.auth.service.AuthenticationService;
import com.rnpc.operatingunit.dto.response.appUser.AppUserResponse;
import com.rnpc.operatingunit.model.AccessRole;
import com.rnpc.operatingunit.model.AppUser;
import com.rnpc.operatingunit.service.AccessRoleService;
import com.rnpc.operatingunit.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final AppUserService appUserService;
    private final AccessRoleService accessRoleService;
    private final ModelMapper modelMapper;

    @PostMapping("/register-user")
    @ResponseStatus(HttpStatus.CREATED)
    public AppUserResponse signUp(@RequestBody @Valid SignUpRequest request) {
        AccessRole accessRole = accessRoleService.findByRole(request.getRoleType());
        AppUser appUser = AppUser.builder()
                .login(request.getLogin())
                .password(request.getPassword())
                .role(accessRole)
                .build();

        return modelMapper.map(appUserService.registerNewUser(appUser), AppUserResponse.class);
    }

    @PostMapping("/login")
    public JwtTokenResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

}
