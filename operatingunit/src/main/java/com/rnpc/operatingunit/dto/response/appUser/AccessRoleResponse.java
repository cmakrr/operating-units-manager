package com.rnpc.operatingunit.dto.response.appUser;

import com.rnpc.operatingunit.enums.AccessRoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRoleResponse {
    private Long id;
    private AccessRoleType role;
    private boolean isSettable;
    private String description;
}
