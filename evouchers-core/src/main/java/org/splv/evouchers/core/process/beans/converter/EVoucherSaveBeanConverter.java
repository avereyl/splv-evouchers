package org.splv.evouchers.core.process.beans.converter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.process.beans.in.EVoucherSaveBean;
import org.springframework.core.convert.converter.Converter;

/**
 * Converter used to provides eVouchers template from real eVouchers.
 * 
 * @author guillaume
 *
 */
public class EVoucherSaveBeanConverter implements Converter<EVoucher, EVoucherSaveBean>{

	@Override
	public EVoucherSaveBean convert(EVoucher source) {
		EVoucherSaveBean destination = new EVoucherSaveBean();
		destination.setVersion(0L);
		destination.setAmount(BigDecimal.ZERO);
		destination.setDonationDate(ZonedDateTime.now());
		destination.setDonorAddress(source.getDonorAddress());
		destination.setDonorCity(source.getDonorCity());
		destination.setDonorEmail(source.getDonorEmail());
		destination.setDonorFirstname(source.getDonorFirstname());
		destination.setDonorLastname(source.getDonorLastname());
		destination.setDonorName(source.getDonorName());
		destination.setDonorType(source.getDonorType());
		destination.setDonorZipcode(source.getDonorZipcode());
		destination.setPaymentMethod(source.getPaymentMethod());
		destination.setOriginalDistributionYear(source.getDistributionYear());
		destination.setDistributionYear(distributionYear(ZonedDateTime.now()));
		destination.setDonorLatitude(source.getDonorCoordinates() != null ? source.getDonorCoordinates().getLatitude() : null);
		destination.setDonorLongitude(source.getDonorCoordinates() != null ? source.getDonorCoordinates().getLongitude() : null);
		return destination;
	}

	public Integer distributionYear(ZonedDateTime zdt) {
		if (zdt.isBefore(ZonedDateTime.of(zdt.getYear(), 6, 30, 0, 0, 0, 0, zdt.getZone()))) {
			return zdt.getYear();
		}
		return zdt.getYear()+1;
	}
}
