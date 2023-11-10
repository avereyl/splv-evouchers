package org.splv.evouchers.core.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.splv.evouchers.core.domain.DocumentType;

@Converter(autoApply = true)
public class DocumentTypeConverter implements AttributeConverter<DocumentType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DocumentType enumeration) {
        return enumeration.getValue();
    }

    @Override
    public DocumentType convertToEntityAttribute(Integer value) {
        return DocumentType.fromValue(value);
    }

}
