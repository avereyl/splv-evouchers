package org.splv.evouchers.core.process.beans.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.EVoucherEvent;
import org.splv.evouchers.core.domain.EVoucherEventType;
import org.splv.evouchers.core.process.beans.out.EVoucherEventObject;

class EVoucherEventObjectConverterTest {

	private EVoucherEventObjectConverter converter = new EVoucherEventObjectConverter();
	
	@Test
	void minimalConversion() {
		//given
		EVoucherEvent event = new EVoucherEvent();
		event.setType(EVoucherEventType.ACK);
		//when
		EVoucherEventObject eventObject = converter.convert(event);
		//then
		assertNotNull(eventObject.getCreatedDate());
		assertNotNull(eventObject.getCreatedBy());
		assertEquals(EVoucherEventType.ACK, eventObject.getEventType());
	}
	
	@Test
	void fullConversion() {
		//given
		String auditor = Constants.DEFAULT_AUDITOR;
		ZonedDateTime createdDate = ZonedDateTime.now();
		EVoucherEvent event = new EVoucherEvent();
		event.setType(EVoucherEventType.NO_OPERATION);
		event.setCreatedDate(createdDate);
		event.setCreatedBy(auditor);
		//when
		EVoucherEventObject eventObject = converter.convert(event);
		//then
		assertEquals(createdDate, eventObject.getCreatedDate());
		assertEquals(auditor, eventObject.getCreatedBy());
		assertEquals(EVoucherEventType.NO_OPERATION, eventObject.getEventType());
		
	}
	
}
