package org.splv.evouchers.core.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.splv.evouchers.core.domain.EVoucherDonorType;

@Converter(autoApply = true)
public class EVoucherDonorTypeConverter implements AttributeConverter<EVoucherDonorType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EVoucherDonorType enumeration) {
        return enumeration.getValue();
    }

    @Override
    public EVoucherDonorType convertToEntityAttribute(Integer value) {
        return EVoucherDonorType.fromValue(value);
    }

}
