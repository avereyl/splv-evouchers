package org.splv.evouchers.core.domain.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.domain.EVoucherEventType;

class EVoucherEventTypeConverterTest {

	private EVoucherEventTypeConverter converter = new EVoucherEventTypeConverter();
	
	@Test
	void everyEnumIsFoundFromItsValue() {
		// given
		for (EVoucherEventType type : EVoucherEventType.values()) {
			// when / then
			assertEquals(type, EVoucherEventType.fromValue(type.getValue()));
			assertEquals(type, converter.convertToEntityAttribute(converter.convertToDatabaseColumn(type)));
		}
	}

	@Test
	void wrongEnumValue_returnUnknownEnum() {
		assertEquals(EVoucherEventType.UNKNOWN, EVoucherEventType.fromValue(-1));
	}
}
