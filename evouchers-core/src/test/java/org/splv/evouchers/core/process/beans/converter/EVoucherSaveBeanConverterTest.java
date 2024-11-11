package org.splv.evouchers.core.process.beans.converter;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.splv.evouchers.core.Constants;
import org.splv.evouchers.core.domain.Coordinates;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.domain.EVoucherDonorType;
import org.splv.evouchers.core.domain.EVoucherIndexingStrategy;
import org.splv.evouchers.core.domain.EVoucherPaymentMethod;
import org.splv.evouchers.core.domain.EVoucherStatus;
import org.splv.evouchers.core.process.beans.in.EVoucherSaveBean;

class EVoucherSaveBeanConverterTest {

	private EVoucherSaveBeanConverter converter = new EVoucherSaveBeanConverter();

	@Test
	void minimalConversion() {
		// given
		EVoucher eVoucher = new EVoucher();
		// when
		EVoucherSaveBean bean = converter.convert(eVoucher);
		// then
		assertEquals(BigDecimal.ZERO, bean.getAmount());
		assertEquals(0L, bean.getVersion());
	}

	@Test
	void fullConversion() {
		// given
		Coordinates coordinates = new Coordinates(47.0, 1.0);
		ZonedDateTime creationDate = ZonedDateTime.now();
		ZonedDateTime donationDate = ZonedDateTime.now().minusHours(2);
		EVoucher eVoucher = new EVoucher();
		eVoucher.setAmount(null);// not copied
		eVoucher.setVersion(null);// not copied
		eVoucher.setCreatedDate(creationDate);
		eVoucher.setLastModifiedDate(creationDate);
		eVoucher.setDistributionYear(2022);
		eVoucher.setDonationDate(donationDate);// not copied
		eVoucher.setDonorName("donor");
		eVoucher.setDonorFirstname("firstname");
		eVoucher.setDonorLastname("lastname");
		eVoucher.setDonorAddress("address");
		eVoucher.setDonorCity("city");
		eVoucher.setDonorZipcode("zipcode");
		eVoucher.setDonorCountrycode("fr");
		eVoucher.setDonorCoordinates(coordinates);
		eVoucher.setDonorEmail("donor@email.org");
		eVoucher.setDonorType(EVoucherDonorType.PROFESSIONAL);
		eVoucher.setPaymentMethod(EVoucherPaymentMethod.CHECK);
		eVoucher.setIndexingStrategy(EVoucherIndexingStrategy.INDEX);
		eVoucher.setStatus(EVoucherStatus.SENT);
		// when
		EVoucherSaveBean bean = converter.convert(eVoucher);
		// then
		assertEquals(BigDecimal.ZERO, bean.getAmount());
		assertEquals(0L, bean.getVersion());

		assertEquals("donor", bean.getDonorName());
		assertEquals("firstname", bean.getDonorFirstname());
		assertEquals("lastname", bean.getDonorLastname());
		assertEquals("address", bean.getDonorAddress());
		assertEquals("city", bean.getDonorCity());
		assertEquals("zipcode", bean.getDonorZipcode());
		assertEquals(coordinates.getLatitude(), bean.getDonorLatitude());
		assertEquals(coordinates.getLongitude(), bean.getDonorLongitude());
		assertEquals("donor@email.org", bean.getDonorEmail());
		assertEquals(EVoucherDonorType.PROFESSIONAL, bean.getDonorType());
		assertEquals(EVoucherPaymentMethod.CHECK, bean.getPaymentMethod());
		assertNotNull(bean.getDonationDate());// set by the converter
		assertNotEquals(bean.getDonationDate(), donationDate);//not copied
		// date set is close to now // tolerance of 5 minutes
		assertTrue(Duration.between(ZonedDateTime.now(Constants.DEFAULT_ZONEID), bean.getDonationDate()).minusMinutes(5)
				.isNegative());
		
		assertNotNull(bean.getOriginalDistributionYear());
		assertNotNull(bean.getDistributionYear());
	}

	@Test
	void whenDateStrictlyBefore3006_thenYear() {
		int y = 2022;
		ZonedDateTime zdt1 = ZonedDateTime.of(y, 6, 29, 0, 0, 0, 0, ZoneOffset.UTC);
		assertEquals(y, converter.distributionYear(zdt1));
	}

	@Test
	void whenDateAfter3006_thenYearPlus1() {
		int y = 2022;
		ZonedDateTime zdt1 = ZonedDateTime.of(y, 6, 30, 0, 0, 0, 0, ZoneOffset.UTC);
		assertEquals(y + 1, converter.distributionYear(zdt1));
	}

}
