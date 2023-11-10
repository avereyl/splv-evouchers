package org.splv.evouchers.core.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.splv.evouchers.core.domain.EVoucherEventType;

@Converter(autoApply = true)
public class EVoucherEventTypeConverter implements AttributeConverter<EVoucherEventType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EVoucherEventType enumeration) {
        return enumeration.getValue();
    }

    @Override
    public EVoucherEventType convertToEntityAttribute(Integer value) {
        return EVoucherEventType.fromValue(value);
    }

}
