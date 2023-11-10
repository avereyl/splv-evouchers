package org.splv.evouchers.core.process.beans.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.domain.Coordinates;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.domain.EVoucherDonorType;
import org.splv.evouchers.core.domain.EVoucherEvent;
import org.splv.evouchers.core.domain.EVoucherPaymentMethod;
import org.splv.evouchers.core.domain.EVoucherStatus;
import org.splv.evouchers.core.process.beans.out.EVoucherObject;


class EVoucherObjectConverterTest {

	private EVoucherObjectConverter converter = new EVoucherObjectConverter();
	
	@Test
	void minimalConversion() {
		// given
		EVoucher eVoucher = new EVoucher();
		// when
		EVoucherObject eVoucherObject = converter.convert(eVoucher);
		// then
		assertEquals(-1, eVoucherObject.getId());
		assertNull(eVoucherObject.getDistributionYear());
		assertNull(eVoucherObject.getDonationDate());
		assertNull(eVoucherObject.getDonorName());
		assertNull(eVoucherObject.getDonorFirstname());
		assertNull(eVoucherObject.getDonorLastname());
		assertNull(eVoucherObject.getDonorAddress());
		assertNull(eVoucherObject.getDonorCity());
		assertNull(eVoucherObject.getDonorZipcode());
		assertNull(eVoucherObject.getDonorLatitude());
		assertNull(eVoucherObject.getDonorLongitude());
		assertNull(eVoucherObject.getDonorEmail());
		assertNull(eVoucherObject.getDonorType());
		assertNull(eVoucherObject.getPaymentMethod());
		
		assertEquals(EVoucherStatus.IN_PROGRESS, eVoucherObject.getStatus());
	}
	
	@Test
	void minimalConversionWithEvent() {
		// given
		EVoucher eVoucher = new EVoucher();
		// when
		EVoucherObject eVoucherObject = converter.convert(eVoucher, List.of(EVoucherEvent.create(eVoucher)));
		// then
		assertEquals(EVoucherStatus.IN_PROGRESS, eVoucherObject.getStatus());
		assertEquals(1, eVoucherObject.getEvents().size());
	}

	@Test
	void fullConversion() {
		// given
		EVoucher eVoucher = new EVoucher();
		Coordinates coordinates = new Coordinates(47.0, 1.0);
		ZonedDateTime donationDate = ZonedDateTime.now();
		eVoucher.setAmount(BigDecimal.TEN);
		eVoucher.setId(1L);
		eVoucher.setVersion(0L);
		eVoucher.setDistributionYear(2022);
		eVoucher.setDonationDate(donationDate);
		eVoucher.setDonorName("donor");
		eVoucher.setDonorFirstname("firstname");
		eVoucher.setDonorLastname("lastname");
		eVoucher.setDonorAddress("address");
		eVoucher.setDonorCity("city");
		eVoucher.setDonorZipcode("zipcode");
		eVoucher.setDonorCoordinates(coordinates);
		eVoucher.setDonorEmail("donor@email.org");
		eVoucher.setDonorType(EVoucherDonorType.PROFESSIONAL);
		eVoucher.setPaymentMethod(EVoucherPaymentMethod.CHECK);
		eVoucher.getEvents().add(EVoucherEvent.create(eVoucher));
		eVoucher.getEvents().add(EVoucherEvent.update(eVoucher));
		eVoucher.getEvents().add(EVoucherEvent.print(eVoucher));
		eVoucher.getEvents().add(EVoucherEvent.dispatch(eVoucher));
		eVoucher.getEvents().add(EVoucherEvent.archive(eVoucher));
		// when
		EVoucherObject eVoucherObject = converter.convert(eVoucher);
		// then
		assertEquals(BigDecimal.TEN, eVoucherObject.getAmount());
		assertEquals(1L, eVoucherObject.getId());
		assertEquals(0L, eVoucherObject.getVersion());
		assertEquals(2022, eVoucherObject.getDistributionYear());
		assertEquals(donationDate, eVoucherObject.getDonationDate());
		assertEquals("donor", eVoucherObject.getDonorName());
		assertEquals("firstname", eVoucherObject.getDonorFirstname());
		assertEquals("lastname", eVoucherObject.getDonorLastname());
		assertEquals("address", eVoucherObject.getDonorAddress());
		assertEquals("city", eVoucherObject.getDonorCity());
		assertEquals("zipcode", eVoucherObject.getDonorZipcode());
		assertEquals(coordinates.getLatitude(), eVoucherObject.getDonorLatitude());
		assertEquals(coordinates.getLongitude(), eVoucherObject.getDonorLongitude());
		assertEquals("donor@email.org", eVoucherObject.getDonorEmail());
		assertEquals(EVoucherDonorType.PROFESSIONAL, eVoucherObject.getDonorType());
		assertEquals(EVoucherPaymentMethod.CHECK, eVoucherObject.getPaymentMethod());
		assertEquals(EVoucherStatus.IN_PROGRESS, eVoucherObject.getStatus());
	}
}
