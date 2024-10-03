package com.rnpc.operatingunit.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequest {
    @Size(min = 5, max = 50)
    @NotBlank
    private String login;
    @Size(min = 3, max = 255)
    @NotBlank
    private String password;
}
