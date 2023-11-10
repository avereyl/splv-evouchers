package org.splv.evouchers.core.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.splv.evouchers.core.domain.EVoucherIndexingStrategy;

@Converter(autoApply = true)
public class EVoucherIndexingStrategyConverter implements AttributeConverter<EVoucherIndexingStrategy, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EVoucherIndexingStrategy enumeration) {
        return enumeration.getValue();
    }

    @Override
    public EVoucherIndexingStrategy convertToEntityAttribute(Integer value) {
        return EVoucherIndexingStrategy.fromValue(value);
    }

}
