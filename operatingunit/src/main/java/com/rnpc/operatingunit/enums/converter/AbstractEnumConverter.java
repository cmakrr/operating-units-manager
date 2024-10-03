package com.rnpc.operatingunit.enums.converter;

import com.rnpc.operatingunit.enums.PersistableEnum;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractEnumConverter<E extends Enum<E> & PersistableEnum<T>, T>
        implements AttributeConverter<E, T> {
    private final Class<E> enumClass;

    public AbstractEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T convertToDatabaseColumn(E e) {
        return Optional.ofNullable(e).map(PersistableEnum::getCode).orElse(null);
    }

    @Override
    public E convertToEntityAttribute(T code) {
        if (Objects.isNull(code)) {
            return null;
        }

        E[] enums = enumClass.getEnumConstants();

        return Arrays.stream(enums)
                .filter(c -> code.equals(c.getCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
