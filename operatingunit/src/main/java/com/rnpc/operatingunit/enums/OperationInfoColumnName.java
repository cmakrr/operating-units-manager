package com.rnpc.operatingunit.enums;

import com.rnpc.operatingunit.enums.converter.AbstractEnumConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OperationInfoColumnName implements PersistableEnum<String> {
    OPERATION_TIME_INTERVAL("OPERATION_TIME_INTERVAL"),
    PATIENT_DETAILS("PATIENT_DETAILS"),
    PATIENT_ROOM_NUMBER("PATIENT_ROOM_NUMBER"),
    OPERATION_NAME("OPERATION_NAME"),
    MEDICAL_WORKERS("MEDICAL_WORKERS"),
    INSTRUMENTS("INSTRUMENTS");

    private final String code;

    @Override
    public String getCode() {
        return code;
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractEnumConverter<OperationInfoColumnName, String> {
        public Converter() {
            super(OperationInfoColumnName.class);
        }
    }
}

