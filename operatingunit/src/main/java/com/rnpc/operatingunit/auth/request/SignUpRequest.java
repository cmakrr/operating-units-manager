package com.rnpc.operatingunit.auth.request;

import com.rnpc.operatingunit.enums.AccessRoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @Size(min = 5, max = 50)
    @NotBlank
    private String login;
    @NotNull
    private AccessRoleType roleType;
    @Size(min = 3, max = 255)
    @NotBlank
    private String password;
}
