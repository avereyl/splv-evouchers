package org.splv.evouchers.core.process.beans.converter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import org.splv.evouchers.core.domain.EVoucher;
import org.splv.evouchers.core.domain.EVoucherEvent;
import org.splv.evouchers.core.process.beans.out.EVoucherEventObject;
import org.splv.evouchers.core.process.beans.out.EVoucherObject;
import org.springframework.core.convert.converter.Converter;

public class EVoucherObjectConverter implements Converter<EVoucher, EVoucherObject> {
	
	private final EVoucherEventObjectConverter eventObjectConverter = new EVoucherEventObjectConverter();
	private final EVoucherDocumentMetadataObjectConverter documentMetadataObjectConverter = new EVoucherDocumentMetadataObjectConverter();

	@Override
	public EVoucherObject convert(EVoucher source) {
		return convert(source, List.of());
	}

	public EVoucherObject convert(EVoucher source, List<EVoucherEvent> events) {

		EVoucherObject destination = new EVoucherObject();
		destination.setId(Objects.isNull(source.getId()) ? -1 : source.getId());
		destination.setVersion(Objects.isNull(source.getVersion())? 0L : source.getVersion());
		destination.setReference(source.getFormattedReference());
		destination.setCreatedDate(source.getCreatedDate().orElseGet(ZonedDateTime::now));
		destination.setLastModifiedDate(source.getLastModifiedDate().orElseGet(ZonedDateTime::now));
		destination.setCreatedBy(source.getCreatedBy().orElse(""));
		destination.setLastModifiedBy(source.getLastModifiedBy().orElse(""));

		destination.setDonationDate(source.getDonationDate());

		destination.setDonorType(source.getDonorType());
		destination.setDonorName(source.getDonorName());
		destination.setDonorFirstname(source.getDonorFirstname());
		destination.setDonorLastname(source.getDonorLastname());
		destination.setDonorAddress(source.getDonorAddress());
		destination.setDonorCity(source.getDonorCity());
		destination.setDonorZipcode(source.getDonorZipcode());
		destination.setDonorEmail(source.getDonorEmail());
		if (source.getDonorCoordinates() != null) {
			destination.setDonorLatitude(source.getDonorCoordinates().getLatitude());
			destination.setDonorLongitude(source.getDonorCoordinates().getLongitude());
		}

		destination.setAmount(source.getAmount());
		destination.setPaymentMethod(source.getPaymentMethod());
		destination.setStatus(source.getStatus());
		destination.setDistributionYear(source.getDistributionYear());

		source.getEvents().forEach(event -> destination.getEvents().add(this.convert(event)));
		events.forEach(event -> destination.getEvents().add(this.convert(event)));
		
		source.getDocuments().forEach((k,v) -> destination.getDocuments().add(documentMetadataObjectConverter.convert(v)));
		
		return destination;
	}

	public EVoucherEventObject convert(EVoucherEvent source) {
		return eventObjectConverter.convert(source);
	}
}
