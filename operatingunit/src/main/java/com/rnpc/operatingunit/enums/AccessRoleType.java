package com.rnpc.operatingunit.enums;

import com.rnpc.operatingunit.enums.converter.AbstractEnumConverter;
import lombok.Getter;

@Getter
public enum AccessRoleType implements PersistableEnum<String> {
    ADMIN("ADMIN", 2),
    GENERAL_MANAGER("GENERAL_MANAGER", 1),
    /**
     * PLAN_CREATOR + STATISTIC_VIEWER
     **/
    MANAGER("MANAGER", 0),
    PLAN_CREATOR("PLAN_CREATOR", 0),
    STATISTIC_VIEWER("STATISTIC_VIEWER", 0),
    TRACKER("TRACKER", 0);

    private final String code;
    private final int hierarchyPosition;

    AccessRoleType(String role, int hierarchyPosition) {
        this.code = role;
        this.hierarchyPosition = hierarchyPosition;
    }

    @Override
    public String getCode() {
        return code;
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractEnumConverter<AccessRoleType, String> {
        public Converter() {
            super(AccessRoleType.class);
        }
    }
}
