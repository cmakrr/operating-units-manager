package com.rnpc.operatingunit.enums;

import com.rnpc.operatingunit.enums.converter.AbstractEnumConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OperationStepName implements PersistableEnum<String> {
    BRINGING_PATIENT("Привоз пациента"),
    OPERATION_BEGIN("Начало операции"),
    ANESTHESIA_INTRODUCTION("Ввод анестезии"),
    SURGEON_WORK_START("Начало работы хирурга"),
    SURGEON_WORK_END("Окончание работы хирурга");

    private final String code;

    @Override
    public String getCode() {
        return code;
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractEnumConverter<OperationStepName, String> {
        public Converter() {
            super(OperationStepName.class);
        }
    }
}
