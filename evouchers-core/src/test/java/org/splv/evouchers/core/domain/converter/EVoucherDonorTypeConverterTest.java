package org.splv.evouchers.core.domain.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.domain.EVoucherDonorType;

class EVoucherDonorTypeConverterTest {
	
	private EVoucherDonorTypeConverter converter = new EVoucherDonorTypeConverter();

	@Test
	void everyEnumIsFoundFromItsValue() {
		// given
		for (EVoucherDonorType type : EVoucherDonorType.values()) {
			// when / then
			assertEquals(type, EVoucherDonorType.fromValue(type.getValue()));
			assertEquals(type, converter.convertToEntityAttribute(converter.convertToDatabaseColumn(type)));
		}
	}

	@Test
	void wrongEnumValue_returnUnknownEnum() {
		assertEquals(EVoucherDonorType.UNKNOWN, EVoucherDonorType.fromValue(-1));
	}
}
