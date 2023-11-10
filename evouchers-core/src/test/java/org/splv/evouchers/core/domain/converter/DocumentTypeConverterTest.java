package org.splv.evouchers.core.domain.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.domain.DocumentType;

class DocumentTypeConverterTest {
	
	private DocumentTypeConverter converter = new DocumentTypeConverter();

	@Test
	void everyEnumIsFoundFromItsValue() {
		// given
		for (DocumentType type : DocumentType.values()) {
			// when / then
			assertEquals(type, DocumentType.fromValue(type.getValue()));
			assertEquals(type, converter.convertToEntityAttribute(converter.convertToDatabaseColumn(type)));
		}
	}

	@Test
	void wrongEnumValue_returnUnknownEnum() {
		assertEquals(DocumentType.UNKNOWN, DocumentType.fromValue(-1));
	}
}
