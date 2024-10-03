package com.rnpc.operatingunit.enums;

import com.rnpc.operatingunit.enums.converter.AbstractEnumConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OperatingRoomStatus implements PersistableEnum<String> {
    FREE("free"),
    OCCUPIED("occupied"),
    NOT_WORK("not work");

    private final String code;

    @Override
    public String getCode() {
        return code;
    }

    @jakarta.persistence.Converter(autoApply = true)
    public static class Converter extends AbstractEnumConverter<OperatingRoomStatus, String> {
        public Converter() {
            super(OperatingRoomStatus.class);
        }
    }
}
