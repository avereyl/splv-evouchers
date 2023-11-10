package org.splv.evouchers.core.process.beans.converter;

import org.splv.evouchers.core.domain.Coordinates;
import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.process.beans.in.EVoucherSaveBean;
import org.springframework.core.convert.converter.Converter;

public class EVoucherConverter implements Converter<EVoucherSaveBean, EVoucher> {

	@Override
	public EVoucher convert(EVoucherSaveBean source) {
		return merge(source, new EVoucher());
	}

	public EVoucher merge(EVoucherSaveBean source, EVoucher destination) {
		destination.setVersion(source.getVersion());
		destination.setAmount(source.getAmount());
		destination.setDonationDate(source.getDonationDate());
		destination.setDonorAddress(source.getDonorAddress());
		destination.setDonorCity(source.getDonorCity());
		destination.setDonorEmail(source.getDonorEmail());
		destination.setDonorFirstname(source.getDonorFirstname());
		destination.setDonorLastname(source.getDonorLastname());
		destination.setDonorName(source.getDonorName());
		destination.setDonorType(source.getDonorType());
		destination.setDonorZipcode(source.getDonorZipcode());
		destination.setPaymentMethod(source.getPaymentMethod());
		destination.setDistributionYear(source.getDistributionYear());
		Coordinates coordinates = source.getDonorLatitude() != null && source.getDonorLongitude() != null
				? new Coordinates(source.getDonorLatitude(), source.getDonorLongitude())
				: null;
		destination.setDonorCoordinates(coordinates);
		return destination;
	}

}
