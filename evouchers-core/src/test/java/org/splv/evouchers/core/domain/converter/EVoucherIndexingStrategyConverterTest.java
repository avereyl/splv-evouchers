package org.splv.evouchers.core.domain.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.domain.EVoucherIndexingStrategy;

class EVoucherIndexingStrategyConverterTest {

	private EVoucherIndexingStrategyConverter converter = new EVoucherIndexingStrategyConverter();

	@Test
	void everyEnumIsFoundFromItsValue() {
		// given
		for (EVoucherIndexingStrategy type : EVoucherIndexingStrategy.values()) {
			// when / then
			assertEquals(type, EVoucherIndexingStrategy.fromValue(type.getValue()));
			assertEquals(type, converter.convertToEntityAttribute(converter.convertToDatabaseColumn(type)));
		}
	}

	@Test
	void wrongEnumValue_returnIndexEnum() {
		assertEquals(EVoucherIndexingStrategy.INDEX, EVoucherIndexingStrategy.fromValue(-1));
	}
}
