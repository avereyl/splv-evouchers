package org.splv.evouchers.core.process.beans.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
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

class EVoucherConverterTest {

	private EVoucherConverter converter = new EVoucherConverter();

	@Test
	void minimalConversion() {
		// given
		EVoucherSaveBean bean = new EVoucherSaveBean();
		// when
		EVoucher eVoucher = converter.convert(bean);
		// then
		assertNull(eVoucher.getDistributionYear());
		assertNull(eVoucher.getDonationDate());
		assertNull(eVoucher.getDonorName());
		assertNull(eVoucher.getDonorFirstname());
		assertNull(eVoucher.getDonorLastname());
		assertNull(eVoucher.getDonorAddress());
		assertNull(eVoucher.getDonorCity());
		assertNull(eVoucher.getDonorZipcode());
		assertNull(eVoucher.getDonorCoordinates());
		assertNull(eVoucher.getDonorEmail());
		assertNull(eVoucher.getDonorType());
		assertNull(eVoucher.getPaymentMethod());
		
		assertEquals(Constants.DEFAULT_LOCALE.getCountry(), eVoucher.getDonorCountrycode());
		assertEquals(EVoucherIndexingStrategy.INDEX, eVoucher.getIndexingStrategy());
		assertEquals(EVoucherStatus.IN_PROGRESS, eVoucher.getStatus());
	}

	@Test
	void fullConversion() {
		// given
		EVoucherSaveBean bean = new EVoucherSaveBean();
		Coordinates coordinates = new Coordinates(47.0, 1.0);
		ZonedDateTime donationDate = ZonedDateTime.now();
		bean.setAmount(BigDecimal.TEN);
		bean.setVersion(0L);
		bean.setDistributionYear(2022);
		bean.setDonationDate(donationDate);
		bean.setDonorName("donor");
		bean.setDonorFirstname("firstname");
		bean.setDonorLastname("lastname");
		bean.setDonorAddress("address");
		bean.setDonorCity("city");
		bean.setDonorZipcode("zipcode");
		bean.setDonorLatitude(coordinates.getLatitude());
		bean.setDonorLongitude(coordinates.getLongitude());
		bean.setDonorEmail("donor@email.org");
		bean.setDonorType(EVoucherDonorType.PROFESSIONAL);
		bean.setPaymentMethod(EVoucherPaymentMethod.CHECK);
		bean.setOriginalDistributionYear(2021);
		// when
		EVoucher eVoucher = converter.convert(bean);
		// then
		assertEquals(BigDecimal.TEN, eVoucher.getAmount());
		assertEquals(0L, eVoucher.getVersion());
		assertEquals(2022, eVoucher.getDistributionYear());
		assertEquals(donationDate, eVoucher.getDonationDate());
		assertEquals("donor", eVoucher.getDonorName());
		assertEquals("firstname", eVoucher.getDonorFirstname());
		assertEquals("lastname", eVoucher.getDonorLastname());
		assertEquals("address", eVoucher.getDonorAddress());
		assertEquals("city", eVoucher.getDonorCity());
		assertEquals("zipcode", eVoucher.getDonorZipcode());
		assertEquals(Constants.DEFAULT_LOCALE.getCountry(), eVoucher.getDonorCountrycode());
		assertEquals(coordinates, eVoucher.getDonorCoordinates());
		assertEquals("donor@email.org", eVoucher.getDonorEmail());
		assertEquals(EVoucherDonorType.PROFESSIONAL, eVoucher.getDonorType());
		assertEquals(EVoucherPaymentMethod.CHECK, eVoucher.getPaymentMethod());
		assertEquals(EVoucherIndexingStrategy.INDEX, eVoucher.getIndexingStrategy());
		assertEquals(EVoucherStatus.IN_PROGRESS, eVoucher.getStatus());
	}
}
