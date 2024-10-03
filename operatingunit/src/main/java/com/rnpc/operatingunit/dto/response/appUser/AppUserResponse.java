package com.rnpc.operatingunit.dto.response.appUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponse {
    private Long id;
    private String login;
    private AccessRoleResponse role;
    private LocalDate registrationDate;
}
