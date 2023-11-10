package org.splv.evouchers.core.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.splv.evouchers.core.domain.EVoucherPaymentMethod;

@Converter(autoApply = true)
public class EVoucherPaymentMethodConverter implements AttributeConverter<EVoucherPaymentMethod, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EVoucherPaymentMethod enumeration) {
        return enumeration.getValue();
    }

    @Override
    public EVoucherPaymentMethod convertToEntityAttribute(Integer value) {
        return EVoucherPaymentMethod.fromValue(value);
    }

}
