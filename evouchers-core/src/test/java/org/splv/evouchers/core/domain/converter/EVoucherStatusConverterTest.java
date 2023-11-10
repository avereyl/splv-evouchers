package org.splv.evouchers.core.domain.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.domain.EVoucherStatus;

class EVoucherStatusConverterTest {

	private EVoucherStatusConverter converter = new EVoucherStatusConverter();
	
	@Test
	void everyEnumIsFoundFromItsValue() {
		// given
		for (EVoucherStatus status : EVoucherStatus.values()) {
			// when / then
			assertEquals(status, EVoucherStatus.fromValue(status.getValue()));
			assertEquals(status, converter.convertToEntityAttribute(converter.convertToDatabaseColumn(status)));
		}
	}

	@Test
	void wrongEnumValue_returnUnknownEnum() {
		assertEquals(EVoucherStatus.UNKNOWN, EVoucherStatus.fromValue(-1));
	}
}
