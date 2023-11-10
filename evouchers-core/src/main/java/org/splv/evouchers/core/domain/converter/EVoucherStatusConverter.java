package org.splv.evouchers.core.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.splv.evouchers.core.domain.EVoucherStatus;

@Converter(autoApply = true)
public class EVoucherStatusConverter implements AttributeConverter<EVoucherStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EVoucherStatus enumeration) {
        return enumeration.getValue();
    }

    @Override
    public EVoucherStatus convertToEntityAttribute(Integer value) {
        return EVoucherStatus.fromValue(value);
    }

}
