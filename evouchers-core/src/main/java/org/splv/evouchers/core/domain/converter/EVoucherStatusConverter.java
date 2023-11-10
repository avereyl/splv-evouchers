package org.splv.evouchers.core.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
