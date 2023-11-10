package org.splv.evouchers.core.domain.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.domain.EVoucherPaymentMethod;

class EVoucherPaymentMethodConverterTest {

	private EVoucherPaymentMethodConverter converter = new EVoucherPaymentMethodConverter();

	@Test
	void everyEnumIsFoundFromItsValue() {
		// given
		for (EVoucherPaymentMethod type : EVoucherPaymentMethod.values()) {
			// when / then
			assertEquals(type, EVoucherPaymentMethod.fromValue(type.getValue()));
			assertEquals(type, converter.convertToEntityAttribute(converter.convertToDatabaseColumn(type)));
		}
	}

	@Test
	void wrongEnumValue_returnIndexEnum() {
		assertEquals(EVoucherPaymentMethod.CASH, EVoucherPaymentMethod.fromValue(-1));
	}
}
