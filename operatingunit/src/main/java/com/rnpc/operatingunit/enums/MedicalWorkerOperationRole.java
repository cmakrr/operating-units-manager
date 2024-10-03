package com.rnpc.operatingunit.enums;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum MedicalWorkerOperationRole implements PersistableEnum<String> {
    OPERATOR("оператор"),
    ASSISTANT("ассистент"),
    TRANSFUSIOLOGIST("трансфузиолог");

    private final String code;

    @Nullable
    public static MedicalWorkerOperationRole getByCode(@Nonnull String code) {
        return Arrays.stream(MedicalWorkerOperationRole.values())
                .filter(role -> code.equalsIgnoreCase(role.code))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getCode() {
        return code;
    }
}
