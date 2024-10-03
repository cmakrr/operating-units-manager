package com.rnpc.operatingunit.enums;

import com.rnpc.operatingunit.enums.converter.AbstractEnumConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OperationStepStatusName implements PersistableEnum<String> {
    NOT_STARTED("not started"),
    STARTED("started"),
    CANCELLED("cancelled"),
    FINISHED("finished");

    private final String code;

    @Override
    public String getCode() {
        return code;
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractEnumConverter<OperationStepStatusName, String> {
        public Converter() {
            super(OperationStepStatusName.class);
        }
    }
}
