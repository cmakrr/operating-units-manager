package com.rnpc.operatingunit.auth.service.impl;

import com.rnpc.operatingunit.auth.request.SignInRequest;
import com.rnpc.operatingunit.auth.response.JwtTokenResponse;
import com.rnpc.operatingunit.auth.service.AuthenticationService;
import com.rnpc.operatingunit.auth.service.JwtService;
import com.rnpc.operatingunit.model.AppUser;
import com.rnpc.operatingunit.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {
    private final AppUserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtTokenResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getPassword()
        ));

        AppUser user = userService.findByLogin(request.getLogin());
        var jwt = jwtService.generateToken(user);

        return new JwtTokenResponse(jwt);
    }
}
