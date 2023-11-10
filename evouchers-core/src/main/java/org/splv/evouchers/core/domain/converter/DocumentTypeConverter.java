package org.splv.evouchers.core.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
