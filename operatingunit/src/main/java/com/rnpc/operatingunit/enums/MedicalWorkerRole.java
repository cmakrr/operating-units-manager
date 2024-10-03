package com.rnpc.operatingunit.enums;

import com.rnpc.operatingunit.enums.converter.AbstractEnumConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MedicalWorkerRole implements PersistableEnum<String> {
    ADMIN("admin"),
    NURSE("nurse"),
    DOCTOR("doctor");

    private final String code;

    @Override
    public String getCode() {
        return code;
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractEnumConverter<MedicalWorkerRole, String> {
        public Converter() {
            super(MedicalWorkerRole.class);
        }
    }
}
