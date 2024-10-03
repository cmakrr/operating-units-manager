package com.rnpc.operatingunit.auth.service;

import com.rnpc.operatingunit.auth.request.SignInRequest;
import com.rnpc.operatingunit.auth.response.JwtTokenResponse;

public interface AuthenticationService {
    JwtTokenResponse signIn(SignInRequest request);
}
